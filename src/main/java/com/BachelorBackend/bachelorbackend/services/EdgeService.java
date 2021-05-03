package com.BachelorBackend.bachelorbackend.services;

import com.BachelorBackend.bachelorbackend.Helpers.NodeConverter;
import com.BachelorBackend.bachelorbackend.Helpers.NodeTreeConverter;
import com.BachelorBackend.bachelorbackend.models.DTOs.EdgesDTO;
import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.ServiceEdge;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EdgeService {

    @Autowired
    TraceService traceService;

    public EdgesDTO getEdgeData(String earliestTime, String latestTime, String filterService){
        ArrayList<Trace> traces = traceService.getAllTraces(earliestTime, latestTime, filterService);
        ArrayList<NodeTree> nodeTrees = NodeTreeConverter.convertTracesToNodeTrees(traces);
        ArrayList<EndpointEdge> endpointEdges = NodeConverter.convertNodeTreesToEndpointEdges(nodeTrees);
        ArrayList<ServiceEdge> serviceEdges = NodeConverter.convertNodeTreesToServiceEdges(nodeTrees);
        return new EdgesDTO(endpointEdges, serviceEdges);
    }
}
