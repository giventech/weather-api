package com.giventech.weather.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}
