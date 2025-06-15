package org.simple.mail.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EncryptedMail extends Mail {
	private String signature;
	private String encryptedAESKey;
	private String encryptedMailContent;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getEncryptedAESKey() {
		return encryptedAESKey;
	}

	public void setEncryptedAESKey(String encryptedAESKey) {
		this.encryptedAESKey = encryptedAESKey;
	}

	public String getEncryptedMailContent() {
		return encryptedMailContent;
	}

	public void setEncryptedMailContent(String encryptedMailContent) {
		this.encryptedMailContent = encryptedMailContent;
	}

    public void setDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);

        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        super.setTime(date);
    }
}