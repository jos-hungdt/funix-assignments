package org.simple.mail.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import org.simple.mail.util.AESUtil;
import org.simple.mail.util.Command;
import org.simple.mail.util.EncryptedMail;
import org.simple.mail.util.FileUtil;
import org.simple.mail.util.Mail;
import org.simple.mail.util.RSAUtil;
import org.simple.mail.util.Request;
import org.simple.mail.util.Response;
import org.simple.mail.util.TcpChannel;

public class UserProcessor {
	private Socket socket;
	private Request request;
	private Response response;
	private TcpChannel channel;

	private static final String SIG_PREFIX = "SIG: ";
	private static final String KEY_PREFIX = "KEY: ";
	private static final String MAIL_PREFIX = "MAIL: ";
	
	public UserProcessor(Socket sock){
		this.socket = sock;
		try {
			channel = new TcpChannel(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int process() throws IOException, OperatorCreationException, PKCSException, InvalidCipherTextException, CryptoException, GeneralSecurityException{
		String command = request.getCommand();
		channel.sendRequest(request);
		response = channel.receiveResponse();
		if (response != null) {
			handleResponse(command);
			return 0;
		}
		else return -1;
	}
	
	public void setResponse(Response res){
		this.response = res;
	}
	
	public void setRequest(Request req){
		this.request = req;
	}
	
	private void handleResponse(String command) throws IOException, OperatorCreationException, PKCSException, InvalidCipherTextException, CryptoException, GeneralSecurityException {
		System.out.println("Receive: " + response.craftToString());
		
		String returnCode = response.getCode();
		if (returnCode.compareTo(Response.SUCCESS) == 0) {
			if (command.compareToIgnoreCase(Command.DATA) == 0) {
				doDataResponse();
			} else if (command.compareToIgnoreCase(Command.LIST) == 0) {
				doListResponse();
			} else if (command.compareToIgnoreCase(Command.RETRIEVE) == 0) {
				doRetrieveResponse();
			}
		}
	}
	
	private void doDataResponse() throws IOException, OperatorCreationException, PKCSException, InvalidCipherTextException, CryptoException, GeneralSecurityException {
		AESUtil aesUtil = new AESUtil();
		RSAUtil rsaUtil = new RSAUtil();

		System.out.println("Send: ");		
		BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder mailContent = new StringBuilder();
		String line;
		
		// 3. Người dùng nhập nội dung email cho tới khi kết thúc bằng “.”
		// Read the mail content from user input until the end marker.
		do {
			line = user.readLine();
			// channel.sendRequest(new Request(line));
			mailContent.append(line).append("\r\n");
		} while (line.compareTo(Mail.END_MAIL) != 0);

		// Create random AES key for encrypting the mail content.
		String randomAESKey = aesUtil.generateRandomKey();
		SecretKey aesKey = aesUtil.getSecretKey(randomAESKey);
		
		// Encrypt the mail content using the AES key.
		String encryptedMailContent = aesUtil.encryptString(aesKey, mailContent.toString());

		// 4. MailClient yêu cầu người dùng nhập đường dẫn file .pem chứa khóa công khai của người nhận
		System.out.println("Enter the path to the recipient's public key file (.pem):");
		String receiverPublicKeyPath = user.readLine();
		if (!FileUtil.isFileExists(receiverPublicKeyPath)) {
			System.out.println("ERROR: File not found: " + receiverPublicKeyPath);
			return;
		}

		// 6. MailClient yêu cầu người dùng nhập đường dẫn file .pem chứa khóa riêng của người dùng
		System.out.println("Enter the path to your private key file (.pem):");
		String senderPrivateKeyPath = user.readLine();
		if (!FileUtil.isFileExists(senderPrivateKeyPath)) {
			System.out.println("ERROR: File not found: " + senderPrivateKeyPath);
			return;
		}

		// 8. MailClient yêu cầu người dùng nhập mật khẩu bảo vệ khóa riêng
		System.out.println("Enter the password for your private key:");
		String password = user.readLine();
		if (!rsaUtil.isPasswordValid(senderPrivateKeyPath, password)) {
			System.out.println("ERROR: Cannot get the private key. Your password may not correct.");
			return;
		}

		RSAKeyParameters privateKey = rsaUtil.getPrivateKey(senderPrivateKeyPath, password);
		RSAKeyParameters receiverPublicKey = rsaUtil.getPublicKey(receiverPublicKeyPath);

		// 9. Mã hóa AES Key bằng khóa công khai của người nhận
		String encryptedAESKey = rsaUtil.encryptString(receiverPublicKey, randomAESKey);

		// RSA Signature
		String signature = rsaUtil.signString(privateKey, encryptedAESKey);

		StringBuilder encryptedMailBuilder = new StringBuilder();
		encryptedMailBuilder.append(SIG_PREFIX).append(signature)
				.append("\n")
				.append(KEY_PREFIX).append(encryptedAESKey)
				.append("\n")
				.append(MAIL_PREFIX).append(encryptedMailContent);

		// Send the mail content to the server
		channel.sendRequest(new Request(encryptedMailBuilder.toString()));

		// Send end mail signal to the server.
        channel.sendRequest(new Request(Command.END_MAIL));

		// 10. MailClient thông báo kết quả gửi email thành công từ MailServer “+OK Mail accepted for delivery”
		response = channel.receiveResponse();
		System.out.println(response.craftToString());
	}
	
	private void doListResponse() throws IOException {
		StringBuilder builder = new StringBuilder();
		int numberOfMail = Integer.parseInt(response.getNotice());
		for(int i = 0; i < numberOfMail; i++)
			builder.append(channel.receiveLine());
		System.out.println(builder.toString());
	}
	
	private void doRetrieveResponse() throws IOException, InvalidCipherTextException, OperatorCreationException, PKCSException, CryptoException, GeneralSecurityException, UnsupportedEncodingException {
		// Retrieve the encrypted mail content from the server.
		EncryptedMail encryptedMail = getEncryptedMail();

		BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
		RSAUtil rsaUtil = new RSAUtil();
		AESUtil aesUtil = new AESUtil();

		// Request the sender's public key file path and validate it
		System.out.println("Enter the path to the sender's public key file (.pem):");
		String senderPublicKeyPath = user.readLine();
		if (!FileUtil.isFileExists(senderPublicKeyPath)) {
			System.out.println("ERROR: File not found: " + senderPublicKeyPath);
			return;
		}

		// Validate signature
		RSAKeyParameters senderPublicKey = rsaUtil.getPublicKey(senderPublicKeyPath);
		if (!rsaUtil.verifyString(senderPublicKey, encryptedMail.getEncryptedAESKey(), encryptedMail.getSignature())) {
			System.out.println("ERROR: Signature verification failed. The mail may have been tampered with.");
			return;
		}

		// Request the the recipent's private key file path and validate it
		System.out.println("Enter the path to your private key file (.pem):");
		String recipientPrivateKeyPath = user.readLine();
		if (!FileUtil.isFileExists(recipientPrivateKeyPath)) {
			System.out.println("ERROR: File not found: " + recipientPrivateKeyPath);
			return;
		}

		// Validate the password for the recipent's private key
		System.out.println("Enter the password for your private key:");
		String password = user.readLine();
		if (!rsaUtil.isPasswordValid(recipientPrivateKeyPath, password)) {
			System.out.println("ERROR: Cannot get the private key. Your password may not correct.");
			return;
		}

		// Decrypt the AES key using the user's private key
		RSAKeyParameters privateKey = rsaUtil.getPrivateKey(recipientPrivateKeyPath, password);
		String decryptedAesKey = rsaUtil.decryptString(privateKey, encryptedMail.getEncryptedAESKey());
        SecretKey aesKey = aesUtil.getSecretKey(decryptedAesKey);

		// Decrypt the mail content using the AES key
		String decryptedMailContent = aesUtil.decryptString(aesKey, encryptedMail.getEncryptedMailContent());

		encryptedMail.setBody(decryptedMailContent);
		System.out.println(encryptedMail.craftToString());
	}

	private EncryptedMail getEncryptedMail() throws IOException {
		EncryptedMail encryptedMail = new EncryptedMail();
		String line;
		int leftBytes = Integer.parseInt(response.getNotice()) + 1;
		while (leftBytes > 0) {
			line = channel.receiveLine();
			if (line.startsWith(SIG_PREFIX)) {
				encryptedMail.setSignature(extractDataFromResponse(line, SIG_PREFIX));
			} else if (line.startsWith(KEY_PREFIX)) {
				encryptedMail.setEncryptedAESKey(extractDataFromResponse(line, KEY_PREFIX));
			} else if (line.startsWith(MAIL_PREFIX)) {
				encryptedMail.setEncryptedMailContent(extractDataFromResponse(line, MAIL_PREFIX));
			} else if (line.startsWith(Mail.FROM)) {
				encryptedMail.setSender(extractDataFromResponse(line, Mail.FROM));
			} else if (line.startsWith(Mail.TO)) {
				encryptedMail.setRecipient(extractDataFromResponse(line, Mail.TO));
			} else if (line.startsWith(Mail.DATE)) {
				encryptedMail.setDate(extractDataFromResponse(line, Mail.DATE));
			}
			leftBytes = leftBytes - line.length();
		}

		return encryptedMail;
	}

	private String extractDataFromResponse(String responseText, String prefix) throws IOException {
		return responseText
			.substring(prefix.length())
			.replace("\n", "")
			.trim();
	}
}
