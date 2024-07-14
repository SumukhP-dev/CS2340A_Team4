package com.example.healthtracker;

public class MockPersonalInformationViewModel {
    private FakeUser user;
    private String gender;

    private MockDatabase mDatabase;

    public MockPersonalInformationViewModel(FakeUser user, MockDatabase database) {
        this.user = user;
        mDatabase = database;
        gender = "male";
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    // Updates personal information or creates a
    // document and enters said personal information in database
    public void addPersonalData(String username, String name, Double height, Double weight,
                                String gender) {
        FakeUser temp = mDatabase.getUser(user);
        if(temp != null) {
            if (user.getName() == null) {
                createUserDocument(mDatabase, username, name, height, weight, gender);
            } else {
                updateDocument(mDatabase, username, name, height, weight, gender);
            }
        }
    }

    // Creates a new user with name, height, weight, gender, and username
    public void createUserDocument(MockDatabase userRef, String username, String name,
                                   Double height, Double weight, String gender) {
        user.setName(name);

        user.setHeight(height);

        user.setWeight(weight);

        user.setGender(gender);
    }

    // Updates personal information of user
    public void updateDocument(MockDatabase userRef, String username, String name,
                               Double height, Double weight, String gender) {
        user.setName(name);

        user.setHeight(height);

        user.setWeight(weight);

        user.setGender(gender);
    }
}
