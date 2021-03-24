package com.BachelorBackend.bachelorbackend.models.nodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Node {
    private String serviceName;
    private String endpointName;
    private String id;
    private List<Node> children;
    //Amount of calls / size of dependency?
}
