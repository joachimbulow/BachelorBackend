package com.BachelorBackend.bachelorbackend.services;

import com.BachelorBackend.bachelorbackend.Helpers.DateHelper;
import com.BachelorBackend.bachelorbackend.Helpers.NodeConverter;
import com.BachelorBackend.bachelorbackend.Helpers.NodeTreeConverter;
import com.BachelorBackend.bachelorbackend.models.DTOs.EdgesDTO;
import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.ServiceEdge;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Span;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class TraceService {

    private final String ZIPKIN_API_URL = "http://joachimbulow.com:9411/zipkin/api/v2/";

    @Autowired
    private RestTemplate restTemplate;

    public ArrayList<Trace> getAllTraces(String earliestTime, String latestTime, String filterService) {
        try {
            ArrayList<Trace> traces = new ArrayList<Trace>();

            //Creating the URL
            long earliestTimeInEpoch = DateHelper.convertDateToEpochMillis(earliestTime);
            long latestTimeInEpoch = DateHelper.convertDateToEpochMillis(latestTime);
            long lookBack = latestTimeInEpoch - earliestTimeInEpoch;
            String url = String.format(ZIPKIN_API_URL + "traces?lookback=%s&endTs=%s&limit=1000" , lookBack, latestTimeInEpoch);
            if (filterService != "") url += "&serviceName=" + filterService;

            ResponseEntity<Span[][]> response = restTemplate.getForEntity(url, Span[][].class);
            //We have to do this manually, as RestTemplate cannot parse directly into Trace type
            for (Span[] trace : response.getBody()) {
                //Escaping CRON jobs who do not have any path and are therefore not relevant
                if(trace[0].getTags().getPath() != null){
                    traces.add(new Trace(Arrays.asList(trace)));
                }

            }
            return traces;
        } catch (Exception e) {
            System.out.println("Exception caught.");
            System.out.println(e.toString());
            return new ArrayList<Trace>();
        }
    }


}
