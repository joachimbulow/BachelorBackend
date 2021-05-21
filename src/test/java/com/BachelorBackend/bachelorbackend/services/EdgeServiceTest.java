package com.BachelorBackend.bachelorbackend.services;

import com.BachelorBackend.bachelorbackend.models.DTOs.EdgesDTO;
import com.BachelorBackend.bachelorbackend.models.responses.Span;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import com.BachelorBackend.bachelorbackend.models.responses.TraceEndpoint;
import com.BachelorBackend.bachelorbackend.models.responses.TraceTags;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class EdgeServiceTest {

    @MockBean
    private TraceService traceService;

    @Autowired
    private EdgeService edgeService;

    private ArrayList<Trace> traces;
    private String sourcePath = "/offices";
    private String targetPath = "/user";

    @BeforeEach
    void setUp() {
        traces = new ArrayList<>();
        List<Span> spans = new ArrayList<>();
        spans.add(generateMockSpan("parent", null, sourcePath));
        spans.add(generateMockSpan("child", "parent", targetPath));
        traces.add(new Trace(spans));
    }

    private Span generateMockSpan(String id, String parentId, String path) {
        TraceEndpoint localEndpoint = new TraceEndpoint("serviceA", "", "", 8080);
        TraceEndpoint remoteEndpoint = new TraceEndpoint("serviceB", "", "", 8080);
        TraceTags traceTags = new TraceTags("GET", path, "MockController", "getMockMethod", null);
        return new Span(id, "traceId", parentId, "name", new Date().getTime(), 10, "SERVER",
                localEndpoint, remoteEndpoint, traceTags, false, false);
    }

    @Test
    void getEdgeData() {
        when(traceService.getAllTraces(any(), any(), any())).thenReturn(traces);
        EdgesDTO edgesDTO = edgeService.getEdgeData("23456", "45678l", "");
        Assertions.assertNotNull(edgesDTO);
        Assertions.assertTrue(
                edgesDTO.getEndpointEdges().stream().anyMatch(endpointEdge -> {
                            return endpointEdge.getSourceEndpoint().getPath().equals(sourcePath)
                                    && endpointEdge.getTargetEndpoint().getPath().equals(targetPath);
                        }));
    }

    @Test
    void getEdgeDataTraceServiceReturnsEmptyList() {
        traces = new ArrayList<>();
        when(traceService.getAllTraces(any(), any(), any())).thenReturn(traces);
        EdgesDTO edgesDTO1 = edgeService.getEdgeData("23456", "345677", "");
        Assertions.assertNotNull(edgesDTO1);
    }
}