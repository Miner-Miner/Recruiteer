package com.miner.recruiteer.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JobPost implements Serializable {
    private String accepted;
    private String ID;
    private String company;
    private String title;
    private String email;
    private String phone;
    private String address;
    private String post;
    private HashMap<String,String> tags;
    private HashMap<String,String> requirements;
    private int totalTags;
    private int totalRequirements;
    private ArrayList<String> tagArray;
    private String salary;
    private String summary;

    public JobPost() {
    }

    public JobPost(String accepted,String ID, String title, String email, String post, HashMap<String, String> tags, HashMap<String, String> requirements, int totalTags, int totalRequirements, ArrayList<String> tagArray, String salary, String summary) {
        this.accepted = accepted;
        this.ID = ID;
        this.title = title;
        this.email = email;
        this.post = post;
        this.tags = tags;
        this.requirements = requirements;
        this.totalTags = totalTags;
        this.totalRequirements = totalRequirements;
        this.tagArray = tagArray;
        this.salary = salary;
        this.summary = summary;
    }

    public JobPost(String accepted, String ID, String company, String title, String email, String phone, String address, String post, HashMap<String, String> tags, HashMap<String, String> requirements, int totalTags, int totalRequirements, ArrayList<String> tagArray, String salary, String summary) {
        this.accepted = accepted;
        this.ID = ID;
        this.company = company;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.post = post;
        this.tags = tags;
        this.requirements = requirements;
        this.totalTags = totalTags;
        this.totalRequirements = totalRequirements;
        this.tagArray = tagArray;
        this.salary = salary;
        this.summary = summary;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
