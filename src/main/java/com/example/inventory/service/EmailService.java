package com.example.inventory.service;


import com.example.inventory.utils.CustomEmailMessage;

public interface EmailService {
    void sendEmail(CustomEmailMessage emailMessage);
}
