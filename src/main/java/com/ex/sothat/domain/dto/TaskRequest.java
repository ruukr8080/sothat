package com.ex.sothat.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskRequest {
    private String title;
    private String description;
    private String assignee;
}