package babbabazrii.com.bababazri.models;


public class FirebaseLocation {

    public String currntLong;

    public String jobId;

    public String jobStatus;

    public String currntLatt;

    public long timestamp;



    public FirebaseLocation(String jobId, String currntLatt, String currntLong, String jobStatus, long timestamp) {
        this.currntLong = currntLong;
        this.jobId = jobId;
        this.jobStatus = jobStatus;
        this.currntLatt = currntLatt;
        this.timestamp = timestamp;
    }

    public FirebaseLocation() {
    }


}