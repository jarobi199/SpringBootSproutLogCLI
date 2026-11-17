package com.garden.model;

import com.garden.enums.YieldUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "harvests")
public class Harvest {
    @Id
    private String id;
    private String userId;
    private String plantId;
    private String plantName;
    private String bedName;
    private String speciesName;
    private LocalDate date;
    private int quantity;
    private YieldUnit yieldUnit;
    private String notes;

    public Harvest() {
        //No argument constructor
    }

    public Harvest(String userId, String plantId, String plantName, String bedName, String speciesName, LocalDate date, int quantity, YieldUnit yieldUnit, String notes) {
        this.userId = userId;
        this.plantId = plantId;
        this.plantName = plantName;
        this.bedName = bedName;
        this.speciesName = speciesName;
        this.date = date;
        this.quantity = quantity;
        this.yieldUnit = yieldUnit;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public YieldUnit getYieldUnit() {
        return yieldUnit;
    }

    public void setYieldUnit(YieldUnit yieldUnit) {
        this.yieldUnit = yieldUnit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
