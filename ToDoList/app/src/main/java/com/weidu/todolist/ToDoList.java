package com.weidu.todolist;

/**
 * Created by adimv on 2016/8/13.
 */
public class ToDoList {
    private String dueDate = "";
    private String title = "";
    private String description = "";
    private String addiInfo = "";
    private long id = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddiInfo() {
        return addiInfo;
    }

    public void setAddiInfo(String addiInfo) {
        this.addiInfo = addiInfo;
    }
}
