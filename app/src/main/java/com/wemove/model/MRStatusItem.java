package com.wemove.model;

import java.util.Date;


public class MRStatusItem {

    private int id;
    private int moveRequestId;
    private MoveStatus moveStatus;
    private Date createdOn;
    private  String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoveRequestId() {
        return moveRequestId;
    }

    public void setMoveRequestId(int moveRequestId) {
        this.moveRequestId = moveRequestId;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }

    public void setMoveStatus(MoveStatus moveStatus) {
        this.moveStatus = moveStatus;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
