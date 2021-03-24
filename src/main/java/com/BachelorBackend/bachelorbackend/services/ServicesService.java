package com.BachelorBackend.bachelorbackend.services;

import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.Service;
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
import java.util.HashMap;

@Component
public class ServicesService {

    private final String ZIPKIN_API_URL = "http://joachimbulow.com:9411/zipkin/api/v2/";

    @Autowired
    private RestTemplate restTemplate;

    public ArrayList<Trace> getAllTraces() {
        try {
            ArrayList<Trace> traces = new ArrayList<Trace>();
            ResponseEntity<Span[][]> response = restTemplate.getForEntity("http://joachimbulow.com:9411/zipkin/api/v2/traces?serviceName=zipkin-server1&endTs=1616277220136&lookback=604800000&limit=10", Span[][].class);
            //We have to do this manually, as RestTemplate cannot parse directly into Trace type
            for (Span[] trace : response.getBody()) {
                traces.add(new Trace(Arrays.asList(trace)));
            }
            return traces;
        } catch (Exception e) {
            System.out.println("Exception caught.");
            System.out.println(e.toString());
            return null;
        }
    }

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

    public ArrayList<NodeTree> convertTracesToNodeTrees(ArrayList<Trace> traces) {
        ArrayList<NodeTree> nodeTrees = new ArrayList<>();
        traces.parallelStream().forEach(trace -> {
            NodeTree nodeTree = new NodeTree();
            //Since root is always last in the array, fetch this initially, and apply as root
            nodeTree.setRootNode(convertServerSpanToNode(trace.getSpans().get(trace.getSpans().size() - 1)));
            //Fill the tree
            fillChildren(nodeTree.getRootNode(), trace);
            nodeTrees.add(nodeTree);
        });
        return nodeTrees;
    }

    private Node convertServerSpanToNode(Span serverSpan) {
        return new Node(serverSpan.getLocalEndpoint().getServiceName(), serverSpan.getTags().getPath(), serverSpan.getId(), new ArrayList<Node>());
    }

    public void fillChildren(Node node, Trace trace) {
        //Since we can derive everything from the SERVER spans, and we have access to ID's we only need these to fill the tree
        trace.getSpans().stream().filter(span -> span.getParentId() == node.getId() && span.getKind() == "SERVER").forEach(span -> {
                    node.getChildren().add(convertServerSpanToNode(span));
                }
        );
        node.getChildren().forEach(childNote -> {
            fillChildren(childNote, trace);
        });
    }

    public ArrayList<EndpointEdge> convertNodeTreesToEndpointEdges(ArrayList<NodeTree> nodeTrees) {
        ArrayList<EndpointEdge> edges = new ArrayList<>();
        nodeTrees.forEach(nodetree -> {
            nodetree.getRootNode();

        });
        return null;
    }

    private void fillEdgesFromNode(Node node, ArrayList<EndpointEdge> edges){

    }

    public void convertNodeTreesToServiceEdges(ArrayList<NodeTree> nodeTrees){

    }
}
