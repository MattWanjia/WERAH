package com.example.matt.werah2;

/**
 * Created by matt on 12/31/2017.
 */

public class Cards {
    private String userID;
    private String jobPosted;

    public Cards(String userID, String jobPosted){
        this.userID = userID;
        this.jobPosted = jobPosted;
    }

    public String getUserID(){
       return userID;
    }
    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getJobPosted(){
        return jobPosted;
    }
    public void setJobPosted(String jobPosted){
        this.jobPosted = jobPosted;
    }
}
