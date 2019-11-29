package com.mastip.personaltodolist.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;

/**
 * Created by HateLogcatError on 8/25/2017.
 */

public class TaskModel implements Parcelable {

    private String id;
    private String task;
    private long time;
    private long finish;
    private int priority;
    private int status;
    private long submission;

    public TaskModel(String id, String task, long time, long finish, int priority, int status, long submission) {
        this.id = id;
        this.task = task;
        this.time = time;
        this.status = status;
        this.finish = finish;
        this.priority = priority;
        this.submission = submission;
    }

    protected TaskModel(Parcel in) {
        id = in.readString();
        task = in.readString();
        time = in.readLong();
        finish = in.readLong();
        priority = in.readInt();
        status = in.readInt();
        submission = in.readLong();
    }

    public TaskModel() {

    }

    public static final Creator<TaskModel> CREATOR = new Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel in) {
            return new TaskModel(in);
        }

        @Override
        public TaskModel[] newArray(int size) {
            return new TaskModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public long getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public long getFinish() {
        return finish;
    }

    public long getSubmission() {
        return submission;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSubmission(long submission) {
        this.submission = submission;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(task);
        parcel.writeLong(time);
        parcel.writeLong(finish);
        parcel.writeInt(priority);
        parcel.writeInt(status);
        parcel.writeLong(submission);
    }
}
