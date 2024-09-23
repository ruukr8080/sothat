package com.ex.sothat.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Task {
    private String title;
    private String description;
    private String assignee;
}