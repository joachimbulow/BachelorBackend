package com.BachelorBackend.bachelorbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EndpointEdge {
    private Endpoint sourceEndpoint;
    private Endpoint targetEndpoint;
    private int count;

    public void incrementCount() {
        this.count++;
    }
}
