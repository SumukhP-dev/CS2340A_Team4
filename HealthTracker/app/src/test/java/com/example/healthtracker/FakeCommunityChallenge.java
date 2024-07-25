package com.example.healthtracker;
import java.util.ArrayList;
import java.util.HashMap;

public class FakeCommunityChallenge {
    private String deadline;
    private String description;
    private String name;
    private HashMap<String, String> participants;
    private ArrayList<FakeWorkoutPlan> workouts;

    public FakeCommunityChallenge(String deadline, String description, String name) {
        this.deadline = deadline;
        this.description = description;
        this.name = name;
        this.participants = new HashMap<String, String>();
        this.workouts = new ArrayList<FakeWorkoutPlan>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasParticipant(String participantName) {
        return participants.containsKey(participantName);
    }

    public void addParticipant(String participantName, String participantStatus) {
        participants.put(participantName, participantStatus);
    }

    public String getParticipantStatus(String participantName) {
        return participants.getOrDefault(participantName, "Participant not found");
    }

    public void setParticipantStatus(String participantName, String participantStatus) {
        participants.replace(participantName, participantStatus);
    }

    public void addWorkout(FakeWorkoutPlan workoutPlan) {
        workouts.add(workoutPlan);
    }

    public ArrayList<FakeWorkoutPlan> getWorkouts() {
        return workouts;
    }
}
