package com.BachelorBackend.bachelorbackend.controllers;

import com.BachelorBackend.bachelorbackend.Helpers.NodeConverter;
import com.BachelorBackend.bachelorbackend.Helpers.NodeTreeConverter;
import com.BachelorBackend.bachelorbackend.models.DTOs.EdgesDTO;
import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.Service;
import com.BachelorBackend.bachelorbackend.models.ServiceEdge;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import com.BachelorBackend.bachelorbackend.services.EdgeService;
import com.BachelorBackend.bachelorbackend.services.ServicesService;
import com.BachelorBackend.bachelorbackend.services.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@CrossOrigin
public class EdgeController {


    @Autowired
    private ServicesService servicesService;

    @Autowired
    private TraceService traceService;

    @Autowired
    private EdgeService edgeService;

    //Returns an array of services in form of objects: {"name":"connect-user-api"}
    @GetMapping("/services")
    public Service[] getServices() {
        return servicesService.getServices();
    }


    //Insert url params to filter
    @GetMapping("/getEdgeData")
    public EdgesDTO getEdgeData(@RequestParam String earliestDate, @RequestParam String latestDate, @RequestParam String filterService) {
        return edgeService.getEdgeData(earliestDate, latestDate, filterService);
    }

    @GetMapping("/testing")
    public void testFunctionality() {
        //Do the work

        //Breakpoint here
        int i = 0;
    }
}
