package com.adisa.diningplus.network.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "managers")
public class Manager implements Serializable {
    @PrimaryKey
    public int id;
    public String name;
    public String email;
    public String position;
}