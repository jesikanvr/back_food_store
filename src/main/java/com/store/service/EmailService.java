package com.store.service;

import com.store.dto.ChangePasswordDTO;
import com.store.dto.EmailValuesDTO;

public interface EmailService {
    public void sendMail(EmailValuesDTO dto);
    public void sendEmailTemplate(EmailValuesDTO dto);
    public String changePassword(ChangePasswordDTO dto);
}
