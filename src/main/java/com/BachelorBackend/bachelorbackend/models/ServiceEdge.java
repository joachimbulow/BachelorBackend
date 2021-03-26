package com.BachelorBackend.bachelorbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceEdge {
    private Service sourceService;
    private Service targetService;
    private int count;

    public void incrementCount(){
        this.count++;
    }
}
