package com.BachelorBackend.bachelorbackend.models.DTOs;

import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.ServiceEdge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class EdgesDTO {
    ArrayList<EndpointEdge> endpointEdges;
    ArrayList<ServiceEdge> serviceEdges;
}
