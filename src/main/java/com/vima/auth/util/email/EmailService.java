package com.vima.auth.util.email;

public interface EmailService {
	void sendSimpleMail(String to, String subject, String body);
}
