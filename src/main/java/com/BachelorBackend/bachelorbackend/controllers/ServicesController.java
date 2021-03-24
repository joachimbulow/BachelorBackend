package com.BachelorBackend.bachelorbackend.controllers;

import com.BachelorBackend.bachelorbackend.models.Service;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import com.BachelorBackend.bachelorbackend.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ServicesController {


    @Autowired
    private ServicesService servicesService;

    private final String ZIPKIN_API_URL = "http://joachimbulow.com:9411/zipkin/api/v2/";


    //Returns an array of services in form of objects: {"name":"connect-user-api"}
    @GetMapping("/services")
    public Service[] getServices() {
        return servicesService.getServices();
    }


    @GetMapping("/services/edges")
    public ArrayList<Trace> getServicesEdges() {
        //Do the work
        //This currently returns traces
        return servicesService.getAllTraces();
    }

    @GetMapping("/testing")
    public String testFunctionality() {
        //Do the work
        ArrayList<Trace> traces = servicesService.getAllTraces();
        ArrayList<NodeTree> nodeTrees = servicesService.convertTracesToNodeTrees(traces);
        return "Check the debug!";
    }
}
