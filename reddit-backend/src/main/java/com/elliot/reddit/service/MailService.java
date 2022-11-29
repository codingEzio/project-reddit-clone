package com.elliot.reddit.service;

import com.elliot.reddit.exception.SpringRedditException;
import com.elliot.reddit.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.elliot.reddit.util.Constants.EMAIL_SENDER_ADDR;

@AllArgsConstructor
@Slf4j
@Service
public class MailService {
	private final JavaMailSender mailSender;
	private final MailContentBuilder mailContentBuilder;

	@Async
	void sendMail(NotificationEmail notificationEmail) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

			messageHelper.setFrom(EMAIL_SENDER_ADDR);
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());

			messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
		};

		try {
			mailSender.send(messagePreparator);

			log.info("Activation email sent!");
		} catch (MailException mailException) {
			throw new SpringRedditException(
					"Exception while sending activation email to "
					+ notificationEmail.getRecipient()
			);
		}
	}
}