package com.example.healthtracker;

//will edit to be able to use MockDatabase
public class MockSignUpViewModel {
    private FakeUser user;
    private String usernameErrorMessage;
    private String passwordErrorMessage;
    private String changedUsername;
    private String generalErrorMessage;
    private Boolean errorMessage;

    public MockSignUpViewModel() {
        changedUsername = null;
        passwordErrorMessage = null;
        usernameErrorMessage = null;
        generalErrorMessage = null;
        errorMessage = null;
    }

    public String getUsernameErrorMessage() {
        return usernameErrorMessage;
    }

    public String getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    public String getGeneralErrorMessage() {
        return generalErrorMessage;
    }

    public void updateUsernameErrorMessage(String error) {
        usernameErrorMessage = error;
    }

    public void updatePasswordErrorMessage(String error) {
        passwordErrorMessage = error;
    }

    public void updateGeneralErrorMessage(String error) {
        generalErrorMessage= error;
    }

    public Boolean getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(boolean statusMessage) {
        this.errorMessage = statusMessage;
    }

    // Sets error messages based on passed in username and password params
    public void checkUsernameAndPassword(String username, String password) {
        boolean correctCredentials = true;

        if (username == null || username.length() == 0) {
            updateUsernameErrorMessage("Username is empty");
            correctCredentials = false;
        } else if (username.trim().length() == 0) {
            updateUsernameErrorMessage("Username cannot be only whitespace");
            correctCredentials = false;
        }

        if (password == null || password.length() == 0) {
            updatePasswordErrorMessage("Password is empty");
            correctCredentials = false;
        } else if (password.trim().length() == 0) {
            updatePasswordErrorMessage("Password cannot be only whitespace");
            correctCredentials = false;
        } else if (password.trim().length() < 6) {
            updatePasswordErrorMessage("Password needs to be 6 char or longer");
            correctCredentials = false;
        }
        setErrorMessage(correctCredentials);
    }

    // Returns true if the username passed in is a valid gmail account
    public boolean validateUsername(String username) {
        return username.toLowerCase().length() > 10 && username.toLowerCase()
                .substring(username.length() - 10).equals("@gmail.com");
    };

    // Creates a user using Firebase Authentication
    public FakeUser signUp(String username, String password) {
        changedUsername = null;
        passwordErrorMessage = null;
        usernameErrorMessage = null;
        generalErrorMessage = null;
        errorMessage = null;

        checkUsernameAndPassword(username, password);
        changedUsername = username;
        if (!(validateUsername(username))) {
            changedUsername = changedUsername + "@gmail.com";
        }

        // Trims username to prevent errors in authentication
        if (changedUsername != null) {
            changedUsername = changedUsername.trim();
        }

        if (!errorMessage) {
            return null;
        }
        user = new FakeUser(cleanUsername(changedUsername), password);

        return user;
    }

    // Cleans username to remove punctuation and the email handle
    public String cleanUsername(String username) {
        String usernameHandleRemoved = username.substring(0,
                changedUsername.length() - 10);
        return usernameHandleRemoved.replaceAll("\\p{P}", "");
    }
}
