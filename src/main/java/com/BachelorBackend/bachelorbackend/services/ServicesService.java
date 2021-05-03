package com.BachelorBackend.bachelorbackend.services;

import com.BachelorBackend.bachelorbackend.Helpers.DateHelper;
import com.BachelorBackend.bachelorbackend.Helpers.NodeConverter;
import com.BachelorBackend.bachelorbackend.models.Endpoint;
import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.Service;
import com.BachelorBackend.bachelorbackend.models.ServiceEdge;
import com.BachelorBackend.bachelorbackend.models.nodes.Node;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Span;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class ServicesService {

    private final String ZIPKIN_API_URL = "http://joachimbulow.com:9411/zipkin/api/v2/";

    @Autowired
    private RestTemplate restTemplate;


    public Service[] getServices() {
        try {
            ResponseEntity<Service[]> response = restTemplate.getForEntity(ZIPKIN_API_URL + "services", Service[].class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Exception caught.");
            System.out.println(e.toString());
            return null;
        }

    }




}
