package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;

public class AbuseReport implements Serializable{
    private String idReporter;
    private String fullnameReporter;
    private String idReported;
    private String fullnameReported;
    private int idCategory;
    private String comment;

    public String getIdReporter() {
        return idReporter;
    }

    public void setIdReporter(String idReporter) {
        this.idReporter = idReporter;
    }

    public String getFullnameReporter() {
        return fullnameReporter;
    }

    public void setFullnameReporter(String fullnameReporter) {
        this.fullnameReporter = fullnameReporter;
    }

    public String getIdReported() {
        return idReported;
    }

    public void setIdReported(String idReported) {
        this.idReported = idReported;
    }

    public String getFullnameReported() {
        return fullnameReported;
    }

    public void setFullnameReported(String fullnameReported) {
        this.fullnameReported = fullnameReported;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
