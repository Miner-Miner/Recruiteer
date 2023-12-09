package com.miner.recruiteer.Class;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateJobPost {
    private String title;
    private String post;
    private HashMap<String,String> tags;
    private HashMap<String,String> requirements;
    private int totalTags;
    private int totalRequirements;
    private ArrayList<String> tagArray;
    private String salary;
    private String summary;

    public UpdateJobPost() {
    }

    public UpdateJobPost(String title, String post, HashMap<String, String> tags, HashMap<String, String> requirements, int totalTags, int totalRequirements, ArrayList<String> tagArray, String salary, String summary) {
        this.title = title;
        this.post = post;
        this.tags = tags;
        this.requirements = requirements;
        this.totalTags = totalTags;
        this.totalRequirements = totalRequirements;
        this.tagArray = tagArray;
        this.salary = salary;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public HashMap<String, String> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<String, String> requirements) {
        this.requirements = requirements;
    }

    public int getTotalTags() {
        return totalTags;
    }

    public void setTotalTags(int totalTags) {
        this.totalTags = totalTags;
    }

    public int getTotalRequirements() {
        return totalRequirements;
    }

    public void setTotalRequirements(int totalRequirements) {
        this.totalRequirements = totalRequirements;
    }

    public ArrayList<String> getTagArray() {
        return tagArray;
    }

    public void setTagArray(ArrayList<String> tagArray) {
        this.tagArray = tagArray;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
