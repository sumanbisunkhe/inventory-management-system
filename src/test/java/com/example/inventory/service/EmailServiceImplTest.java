package com.example.inventory.service;

import com.example.inventory.service.impl.EmailServiceImpl;
import com.example.inventory.utils.CustomEmailMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail_Success() {
        // Create a CustomEmailMessage
        CustomEmailMessage emailMessage = new CustomEmailMessage();
        emailMessage.setFrom("sender@example.com");
        emailMessage.setTo("recipient@example.com");
        emailMessage.setSubject("Test Subject");
        emailMessage.setText("This is a test email.");
        emailMessage.setSentDate(new Date());

        // Capture the argument passed to JavaMailSender's send method
        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Test the sendEmail method
        emailService.sendEmail(emailMessage);

        // Verify send was called once
        verify(mailSender, times(1)).send(emailCaptor.capture());

        // Retrieve the captured SimpleMailMessage
        SimpleMailMessage actualEmail = emailCaptor.getValue();

        // Assertions to verify the captured email fields
        assertEquals(emailMessage.getFrom(), actualEmail.getFrom());
        assertEquals(emailMessage.getTo(), actualEmail.getTo()[0]);
        assertEquals(emailMessage.getSubject(), actualEmail.getSubject());
        assertEquals(emailMessage.getText(), actualEmail.getText());
        assertEquals(emailMessage.getSentDate(), actualEmail.getSentDate());
    }
}
