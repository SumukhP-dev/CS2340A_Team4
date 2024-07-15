package com.example.healthtracker;

import java.util.ArrayList;

//methods will be added on an as need basis to best be
// able to support functions in the Mock view models
public class MockDatabase {
    private ArrayList<FakeUser> users;

    public MockDatabase() {
        users = new ArrayList<FakeUser>();
    }

    public void addUser(FakeUser user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public FakeUser getUser(FakeUser user) {
        if (users.contains(user)) {
            int index = users.indexOf(user);
            return users.get(index);
        } else {
            return null;
        }
    }

    public FakeUser removeUser(FakeUser user) {
        if (users.contains(user)) {
            int index = users.indexOf(user);
            return users.remove(index);
        } else {
            return null;
        }
    }
}
