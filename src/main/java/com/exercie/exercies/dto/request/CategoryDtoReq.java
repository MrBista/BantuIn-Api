package com.exercie.exercies.dto.request;

import java.time.LocalDateTime;

public class CategoryDtoReq {
    private String name;
    private String description;

    public CategoryDtoReq() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CategoryDtoReq{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
