package com.yummy.foodonmytrain;

import java.io.Serializable;

public class Personal_details implements Serializable {

    private String mId;
    private String mFirstname;
    private String mMiddlename;
    private String mLastname;
    private String mUserType;
    private String mPersonalno;
    private String mPersonalDOB;
    private String mTrainNo;


    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public String getMiddlename() {
        return mMiddlename;
    }

    public void setMiddlename(String mMiddlename) {
        this.mMiddlename = mMiddlename;
    }

    public String getLastname() {
        return mLastname;
    }

    public void setLastname(String mLastname) {
        this.mLastname = mLastname;
    }

    public String getUserType() {
        return mUserType;
    }

    public void setUserType(String mGender) {
        this.mUserType = mGender;
    }

    public String getPersonalno() {
        return mPersonalno;
    }

    public void setPersonalno(String mPersonalno) {
        this.mPersonalno = mPersonalno;
    }

    public String getPersonalDOB() {
        return mPersonalDOB;
    }

    public void setPersonalDOB(String mPersonalDOB) {
        this.mPersonalDOB = mPersonalDOB;
    }

    public String getTrainNo() {
        return mTrainNo;
    }

    public void setTrainNo(String mTrainNo) {
        this.mTrainNo = mTrainNo;
    }


    @Override
    public String toString() {
        return "Personal_details{" +
                ", mFirstname='" + mFirstname + '\'' +
                ", mMiddlename='" + mMiddlename + '\'' +
                ", mLastname='" + mLastname + '\'' +
                ", mUserType='" + mUserType + '\'' +
                ", mPersonalno='" + mPersonalno + '\'' +
                ", mPersonalDOB='" + mPersonalDOB + '\'' +
                '}';
    }
}
