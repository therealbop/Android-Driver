package com.karry.pojo;


import java.io.Serializable;

public class QueuePosition implements Serializable{

    private int queuePosition;

    private int queueLength;

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    public int getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(int queueLength) {
        this.queueLength = queueLength;
    }
}
