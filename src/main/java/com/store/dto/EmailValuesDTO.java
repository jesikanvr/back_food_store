package com.store.dto;

public class EmailValuesDTO {

    private String emailFrom;
    private String emailTo;
    private String subject;
    private String content;
    private String userName;
    private String tokenPassword;

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailValuesDTO() {
    }

    public EmailValuesDTO(String emailFrom, String emailTo, String userName, String tokenPassword) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.userName = userName;
        this.tokenPassword = tokenPassword;
    }

    public EmailValuesDTO(String emailFrom, String emailTo, String subject, String content, String userName) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.subject = subject;
        this.content = content;
        this.userName = userName;
    }

}
