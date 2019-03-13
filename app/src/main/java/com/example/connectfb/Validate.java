package com.example.connectfb;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import java.util.regex.Pattern;


public class Validate {
    public static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                    +"\\@"+
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+
                    "("+
                    "\\."+
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+");
    public static final Pattern USERNAME = Pattern.compile("[a-zA-Z ]+");
    public  static  final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            //"(?=.*[0-9])" +         //at least 1 digit
            //"(?=.*[a-z])" +         //at least 1 lower case letter
            //"(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");
    public boolean validateEmail(String emailInput, TextInputLayout textInputEmail) {
        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
    public boolean validateUsername(String usernameInput,TextInputLayout textInputNAME ) {
        if (usernameInput.isEmpty()) {
            textInputNAME.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 30) {
            textInputNAME.setError("Username too long");
            return false;
        } else if(!USERNAME.matcher(usernameInput).matches()){
            textInputNAME.setError("Please enter a valid name ");
            return false;
        }
        else {
            textInputNAME.setError(null);
            return true;
        }
    }
    public boolean validatePassword(String passwordInput, TextInputLayout textInputPassword) {
        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        }

        else {
            textInputPassword.setError(null);
            return true;
        }
    }
    public boolean validateConfirmPassword(String passwordInput, String confirmPassword, TextInputLayout textInputConfirmPassword ) {
        if (passwordInput.isEmpty()) {
            textInputConfirmPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputConfirmPassword.setError("Password too weak");
            return false;
        }
        else if(!confirmPassword.equals(passwordInput)){
            textInputConfirmPassword.setError("Password not matching");
            return  false;
        }
        else {
            textInputConfirmPassword.setError(null);
            return true;
        }
    }

}
