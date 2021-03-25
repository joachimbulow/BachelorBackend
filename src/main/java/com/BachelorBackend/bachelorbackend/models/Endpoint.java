package com.BachelorBackend.bachelorbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Endpoint {
    private String path;
    private String serviceName;

}
