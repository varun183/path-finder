package com.pathfinder.pathfinderbackend.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateMapRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Min(value = 5, message = "Width must be at least 5")
    @Max(value = 50, message = "Width cannot exceed 50")
    private int width;

    @Min(value = 5, message = "Height must be at least 5")
    @Max(value = 50, message = "Height cannot exceed 50")
    private int height;

    @Min(value = 0, message = "Obstacle density cannot be negative")
    @DecimalMax(value = "0.5", message = "Obstacle density cannot exceed 0.5")
    private double obstacleDensity = 0.2;
}
