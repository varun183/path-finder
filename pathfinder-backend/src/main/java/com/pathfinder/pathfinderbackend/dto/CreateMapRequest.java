package com.pathfinder.pathfinderbackend.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMapRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Min(value = 5, message = "Width must be at least 5")
    @Max(value = 100, message = "Width cannot exceed 100")
    private int width;

    @Min(value = 5, message = "Height must be at least 5")
    @Max(value = 100, message = "Height cannot exceed 100")
    private int height;
}