package com.example.healthtracker.view;

public interface CommunitySubject {

    void addObserver(CommunityObserver observer);
    void removeObserver(CommunityObserver observer);

    void notifyObservers(String participantID, String status);


}
