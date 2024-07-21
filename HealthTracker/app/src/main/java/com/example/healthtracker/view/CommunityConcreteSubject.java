package com.example.healthtracker.view;

import java.util.ArrayList;
import java.util.List;

public class CommunityConcreteSubject implements CommunitySubject{

    private List<CommunityObserver> observerList;
    // private String status;

    public CommunityConcreteSubject() {
        observerList = new ArrayList<>();
    }

    @Override
    public void addObserver(CommunityObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(CommunityObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(String participantID, String status) {
        for (CommunityObserver observer : observerList) {
            observer.update(participantID, status);
        }
    }

}
