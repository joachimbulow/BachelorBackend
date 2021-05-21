package com.BachelorBackend.bachelorbackend.controllers;

import com.BachelorBackend.bachelorbackend.models.DTOs.EdgesDTO;
import com.BachelorBackend.bachelorbackend.models.Service;
import com.BachelorBackend.bachelorbackend.services.EdgeService;
import com.BachelorBackend.bachelorbackend.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class EdgeController {


    @Autowired
    private ServicesService servicesService;

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
}
