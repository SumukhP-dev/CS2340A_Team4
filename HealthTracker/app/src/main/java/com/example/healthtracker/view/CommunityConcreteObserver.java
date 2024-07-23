package com.example.healthtracker.view;

import android.widget.TextView;

public class CommunityConcreteObserver implements CommunityObserver {
    private String participantID;
    private TextView statusTextView;

    public CommunityConcreteObserver(String participantID, TextView statusTextView) {
        this.participantID = participantID;
        this.statusTextView = statusTextView;
    }

    @Override
    public void update(String updatedParticipantID, String status) {
        if (this.participantID.equals(updatedParticipantID)) {
            statusTextView.setText(status);
        }
    }

}
