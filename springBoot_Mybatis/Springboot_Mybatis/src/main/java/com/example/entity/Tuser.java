package com.example.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Tuser implements java.io.Serializable {

    private String id;
    private Date createdatetime;
    private Date modifydatetime;
    private String name;
    private String pwd;
    private Set<String> troles = new HashSet<String>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(Date createdatetime) {
        this.createdatetime = createdatetime;
    }

    public Date getModifydatetime() {
        return modifydatetime;
    }

    public void setModifydatetime(Date modifydatetime) {
        this.modifydatetime = modifydatetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Set<String> getTroles() {
        return troles;
    }

    public void setTroles(Set<String> troles) {
        this.troles = troles;
    }


}
