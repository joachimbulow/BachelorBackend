package com.BachelorBackend.bachelorbackend.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraceTags {
    @JsonProperty(value="http.method")
    private String method;
    @JsonProperty(value="http.path")
    private String path;
    @JsonProperty(value="mvc.controller.class")
    private String controllerClass;
    @JsonProperty(value="mvc.controller.method")
    private String controllerMethod;

}
