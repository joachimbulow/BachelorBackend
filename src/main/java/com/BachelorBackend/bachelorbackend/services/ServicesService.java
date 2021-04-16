package com.BachelorBackend.bachelorbackend.services;

import com.BachelorBackend.bachelorbackend.Helpers.DateHelper;
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

    public ArrayList<Trace> getAllTraces(String earliestTime, String latestTime, String filterService) {
        try {
            ArrayList<Trace> traces = new ArrayList<Trace>();

            //Creating the URL
            long earliestTimeInEpoch = DateHelper.convertDateToEpochMillis(earliestTime);
            long latestTimeInEpoch = DateHelper.convertDateToEpochMillis(latestTime);
            long lookBack = latestTimeInEpoch - earliestTimeInEpoch;
            String url = String.format("http://joachimbulow.com:9411/zipkin/api/v2/traces?lookback=%s&endTs=%s" , lookBack, latestTimeInEpoch);
            if (filterService != "") url += "&serviceName=" + filterService;

            ResponseEntity<Span[][]> response = restTemplate.getForEntity(url, Span[][].class);
            //We have to do this manually, as RestTemplate cannot parse directly into Trace type
            for (Span[] trace : response.getBody()) {
                traces.add(new Trace(Arrays.asList(trace)));
            }
            return traces;
        } catch (Exception e) {
            System.out.println("Exception caught.");
            System.out.println(e.toString());
            return new ArrayList<Trace>();
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
        traces.stream().forEach(trace -> {
            NodeTree nodeTree = new NodeTree();
            //Find the root span, that doesn't have a parent
            Span rootSpan = trace.getSpans().stream().filter(span -> span.getParentId() == null).findFirst().orElse(null);
            if (rootSpan == null) return;
            
            nodeTree.setRootNode(convertServerSpanToNode(rootSpan));
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
        trace.getSpans().stream().filter(span -> (span.getParentId() != null && span.getParentId().equals(node.getId())) && span.getKind().equals("SERVER")).forEach(span -> {
                    node.getChildren().add(convertServerSpanToNode(span));
                }
        );
        node.getChildren().forEach(childNote -> {
            fillChildren(childNote, trace);
        });
    }

    public ArrayList<EndpointEdge> convertNodeTreesToEndpointEdges(ArrayList<NodeTree> nodeTrees) {
        HashMap<String, EndpointEdge> edgeMap = new HashMap<>();

        nodeTrees.forEach(nodetree -> {
            fillEndpointEdgeHashMapFromNode(nodetree.getRootNode(), edgeMap);
        });

        return new ArrayList<EndpointEdge>(edgeMap.values());
    }

    public ArrayList<ServiceEdge> convertNodeTreesToServiceEdges(ArrayList<NodeTree> nodeTrees) {
        HashMap<String, ServiceEdge> edgeMap = new HashMap<>();

        nodeTrees.forEach(nodeTree -> {
            fillServiceEdgeHashMapFromNode(nodeTree.getRootNode(), edgeMap);
        });

        return new ArrayList<ServiceEdge>(edgeMap.values());

    }

    private void fillEndpointEdgeHashMapFromNode(Node node, HashMap<String, EndpointEdge> edgeMap) {
        //Depth first search -ish
        node.getChildren().forEach(childNode -> {
            String edgeKeyString = node.getServiceName() + "/" + node.getEndpointName() + "/" + childNode.getServiceName() + "/" + childNode.getEndpointName();
            if (!edgeMap.containsKey(edgeKeyString)){
                edgeMap.put(edgeKeyString, new EndpointEdge(nodeToEndpoint(node), nodeToEndpoint(childNode), 1));
            }
            else {
                edgeMap.get(edgeKeyString).incrementCount();
            }

            fillEndpointEdgeHashMapFromNode(childNode, edgeMap);

        });
    }

    private void fillServiceEdgeHashMapFromNode(Node node, HashMap<String, ServiceEdge> edgeMap){
        node.getChildren().forEach(childNode -> {
            String edgeKeyString = node.getServiceName() + "/" + childNode.getServiceName();
            if (!edgeMap.containsKey(edgeKeyString)){
                edgeMap.put(edgeKeyString, new ServiceEdge(nodeToService(node), nodeToService(childNode), 1));
            }
            else {
                edgeMap.get(edgeKeyString).incrementCount();
            }

            fillServiceEdgeHashMapFromNode(childNode, edgeMap);
        });
    }

    private Endpoint nodeToEndpoint(Node node){
        return new Endpoint(node.getEndpointName(), node.getServiceName());
    }

    private Service nodeToService(Node node){
        return new Service(node.getServiceName());
    }


}
