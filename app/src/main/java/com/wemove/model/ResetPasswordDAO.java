package com.wemove.model;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordDAO {

    @SerializedName("email")
    private String email;
    @SerializedName("securityQuestion")
    private String securityQuestion;
    @SerializedName("secretAnswer")
    private String secretAnswer;
    @SerializedName("newPassword")
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
