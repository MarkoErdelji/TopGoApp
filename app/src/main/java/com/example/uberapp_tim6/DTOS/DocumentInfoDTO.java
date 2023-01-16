package com.example.uberapp_tim6.DTOS;

import com.example.uberapp_tim6.models.Document;

public class DocumentInfoDTO {
    private Integer id;
    private String name;
    private String documentImage;
    private Integer driverId;

    public DocumentInfoDTO() {
    }

    public DocumentInfoDTO(Document document) {
        this.id = document.getId();
        this.name = document.getName();
        this.documentImage = document.getDocumentImage();
        this.driverId = document.getDriver().getId();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }
}
