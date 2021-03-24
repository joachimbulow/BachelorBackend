package com.BachelorBackend.bachelorbackend.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Span {
    private String id;
    private String traceId;
    private String parentId;
    private String name;
    private long timestamp;
    private int duration;
    private String kind;
    private TraceEndpoint localEndpoint;
    private TraceEndpoint remoteEndpoint;
    private TraceTags tags;
    private boolean shared;
    private boolean debug;

}
