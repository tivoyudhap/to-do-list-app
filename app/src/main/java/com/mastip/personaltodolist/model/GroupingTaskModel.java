package com.mastip.personaltodolist.model;

import java.util.List;

/**
 * Created by HateLogcatError on 9/4/2017.
 */

public class GroupingTaskModel {

    private String groupName;
    private List<TaskModel> taskModel;
    private String date;

    public GroupingTaskModel(String groupName, List<TaskModel> taskModel, String date) {
        this.groupName = groupName;
        this.taskModel = taskModel;
        this.date = date;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<TaskModel> getTaskModel() {
        return taskModel;
    }

    public String getDate() {
        return date;
    }
}
