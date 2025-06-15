package org.simple.mail.util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAUtil {
    public RSAKeyParameters getPrivateKey(String keyFile, String password) throws IOException, OperatorCreationException, PKCSException {
        RSAKeyParameters privateKey = null;
        PrivateKeyInfo keyInfo = null;
        FileReader reader = new FileReader(keyFile);
        PEMParser pemParser = new PEMParser(reader);
        Object keyPair = pemParser.readObject();
        if (keyPair instanceof PKCS8EncryptedPrivateKeyInfo) {
            JceOpenSSLPKCS8DecryptorProviderBuilder jce = new JceOpenSSLPKCS8DecryptorProviderBuilder();
            jce.setProvider(new BouncyCastleProvider());
            InputDecryptorProvider decProv = jce.build(password.toCharArray());
            keyInfo = ((PKCS8EncryptedPrivateKeyInfo) keyPair).decryptPrivateKeyInfo(decProv);
        } else if (keyPair instanceof PrivateKeyInfo) {
            keyInfo = (PrivateKeyInfo) keyPair;
        }
        privateKey = (RSAKeyParameters) PrivateKeyFactory.createKey(keyInfo);

        pemParser.close();
        return privateKey;
    }

    public boolean isPasswordValid(String keyFile, String password) {
        try {
            getPrivateKey(keyFile, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public RSAKeyParameters getPublicKey(String certFile) {
        RSAKeyParameters publicKey = null;
        try (FileReader reader = new FileReader(certFile);
             PEMParser pemParser = new PEMParser(reader)) {
            X509CertificateHolder certificate;
            certificate = (X509CertificateHolder) pemParser.readObject();
            publicKey = (RSAKeyParameters) PublicKeyFactory.createKey(certificate.getSubjectPublicKeyInfo());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return publicKey;
    }

    public String encryptString(RSAKeyParameters publicKey, String plainText) throws InvalidCipherTextException {
        String cipherText;
        cipherText = Base64.toBase64String(encryptBytes(publicKey, plainText.getBytes(StandardCharsets.UTF_8)));
        return cipherText;
    }

    public String decryptString(RSAKeyParameters privateKey, String cipherText) throws InvalidCipherTextException {
        byte[] cipherBytes = Base64.decode(cipherText);
        return new String(decryptBytes(privateKey, cipherBytes), StandardCharsets.UTF_8);
    }

    public byte[] encryptBytes(RSAKeyParameters publicKey, byte[] plainBytes) throws InvalidCipherTextException {
        // Dung chuan OAEP
        OAEPEncoding cipher = new OAEPEncoding(new RSAEngine());
        cipher.init(true, publicKey);
        return cipher.processBlock(plainBytes, 0, plainBytes.length);
    }

    public byte[] decryptBytes(RSAKeyParameters privateKey, byte[] cipherBytes) throws InvalidCipherTextException {
        OAEPEncoding cipher = new OAEPEncoding(new RSAEngine());
        cipher.init(false, privateKey);
        return cipher.processBlock(cipherBytes, 0, cipherBytes.length);
    }

    public String signString(RSAKeyParameters key, String input) throws DataLengthException, CryptoException {
        byte[] signature = signBytes(key, input.getBytes(StandardCharsets.UTF_8));
        return Base64.toBase64String(signature);
    }

    public boolean verifyString(RSAKeyParameters key, String input, String signature) {
        return verifyBytes(key, input.getBytes(StandardCharsets.UTF_8), Base64.decode(signature));
    }

    public byte[] signBytes(RSAKeyParameters key, byte[] inputBytes)
            throws DataLengthException, CryptoException {
        RSADigestSigner signer = new RSADigestSigner(new SHA256Digest());
        signer.init(true, key);
        signer.update(inputBytes, 0, inputBytes.length);
        return signer.generateSignature();
    }

    public boolean verifyBytes(RSAKeyParameters key, byte[] inputBytes, byte[] signature){
        RSADigestSigner verifier = new RSADigestSigner(new SHA256Digest());
        verifier.init(false, key);
        verifier.update(inputBytes, 0, inputBytes.length);
        return verifier.verifySignature(signature);
    }
}
