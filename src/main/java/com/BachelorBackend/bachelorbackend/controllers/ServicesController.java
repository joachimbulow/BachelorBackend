package com.BachelorBackend.bachelorbackend.controllers;

import com.BachelorBackend.bachelorbackend.models.DTOs.EdgesDTO;
import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.Service;
import com.BachelorBackend.bachelorbackend.models.ServiceEdge;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import com.BachelorBackend.bachelorbackend.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
@CrossOrigin
public class ServicesController {


    @Autowired
    private ServicesService servicesService;

    private final String ZIPKIN_API_URL = "http://joachimbulow.com:9411/zipkin/api/v2/";


    //Returns an array of services in form of objects: {"name":"connect-user-api"}
    @GetMapping("/services")
    public Service[] getServices() {
        return servicesService.getServices();
    }


//    @GetMapping("/traces")
//    public ArrayList<Trace> getServicesEdges() {
//        //Do the work
//        return servicesService.getAllTraces();
//    }

    //Insert url params to filter
    @GetMapping("/getEdgeData")
    public EdgesDTO getEdgeData(@RequestParam String earliestDate, @RequestParam String latestDate, @RequestParam String filterService) {
        ArrayList<Trace> traces = servicesService.getAllTraces(earliestDate, latestDate, filterService);
        ArrayList<NodeTree> nodeTrees = servicesService.convertTracesToNodeTrees(traces);
        ArrayList<EndpointEdge> endpointEdges = servicesService.convertNodeTreesToEndpointEdges(nodeTrees);
        ArrayList<ServiceEdge> serviceEdges = servicesService.convertNodeTreesToServiceEdges(nodeTrees);
        return new EdgesDTO(endpointEdges, serviceEdges);
    }

    @GetMapping("/testing")
    public void testFunctionality() {
        //Do the work

        //Breakpoint here
        int i = 0;
    }
}
