package com.BachelorBackend.bachelorbackend.Helpers;

import com.BachelorBackend.bachelorbackend.models.Endpoint;
import com.BachelorBackend.bachelorbackend.models.EndpointEdge;
import com.BachelorBackend.bachelorbackend.models.Service;
import com.BachelorBackend.bachelorbackend.models.ServiceEdge;
import com.BachelorBackend.bachelorbackend.models.nodes.Node;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Span;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeConverter {

    public static Endpoint nodeToEndpoint(Node node){
        return new Endpoint(node.getEndpointName(), node.getServiceName());
    }

    public static Service nodeToService(Node node){
        return new Service(node.getServiceName());
    }

    public static Node convertServerSpanToNode(Span serverSpan) {
        return new Node(serverSpan.getLocalEndpoint().getServiceName(), serverSpan.getName(), serverSpan.getId(), new ArrayList<Node>());
    }

    public static ArrayList<ServiceEdge> convertNodeTreesToServiceEdges(ArrayList<NodeTree> nodeTrees) {
        HashMap<String, ServiceEdge> edgeMap = new HashMap<>();

        nodeTrees.forEach(nodetree -> {
            Node rootNode = nodetree.getRootNode();
            // If the root node has no children, add it as an edge with an empty target for FE to get Node from
            if (rootNode.getChildren().size() == 0){
                String rootNodeKeyString = rootNode.getServiceName();
                if (!edgeMap.containsKey(rootNodeKeyString)){
                    edgeMap.put(rootNodeKeyString, new ServiceEdge(NodeConverter.nodeToService(rootNode), null, 1));
                }
                else {
                    edgeMap.get(rootNodeKeyString).incrementCount();
                }
            }
            else {
                // Else fill edges
                fillServiceEdgeHashMapFromNode(rootNode, edgeMap);
            }
        });

        return new ArrayList<ServiceEdge>(edgeMap.values());

    }

    private static void fillServiceEdgeHashMapFromNode(Node node, HashMap<String, ServiceEdge> edgeMap){
        node.getChildren().forEach(childNode -> {
            String edgeKeyString = node.getServiceName() + "/" + childNode.getServiceName();
            if (!edgeMap.containsKey(edgeKeyString)){
                edgeMap.put(edgeKeyString, new ServiceEdge(NodeConverter.nodeToService(node), NodeConverter.nodeToService(childNode), 1));
            }
            else {
                edgeMap.get(edgeKeyString).incrementCount();
            }

            fillServiceEdgeHashMapFromNode(childNode, edgeMap);
        });
    }

    public static ArrayList<EndpointEdge> convertNodeTreesToEndpointEdges(ArrayList<NodeTree> nodeTrees) {
        HashMap<String, EndpointEdge> edgeMap = new HashMap<>();

        nodeTrees.forEach(nodetree -> {
            Node rootNode = nodetree.getRootNode();
            // If the root node has no children, add it as an edge with an empty target for FE to get Node from
            if (rootNode.getChildren().size() == 0){
                String rootNodeKeyString = rootNode.getServiceName() + "/" + rootNode.getEndpointName();
                if (!edgeMap.containsKey(rootNodeKeyString)){
                    edgeMap.put(rootNodeKeyString, new EndpointEdge(NodeConverter.nodeToEndpoint(rootNode), null, 1));
                }
                else {
                    edgeMap.get(rootNodeKeyString).incrementCount();
                }
            }
            else {
                // Else fill edges
                fillEndpointEdgeHashMapFromNode(rootNode, edgeMap);
            }

        });

        return new ArrayList<EndpointEdge>(edgeMap.values());
    }

    private static void fillEndpointEdgeHashMapFromNode(Node node, HashMap<String, EndpointEdge> edgeMap) {
        //Depth first search -ish
        node.getChildren().forEach(childNode -> {
            String edgeKeyString = node.getServiceName() + "/" + node.getEndpointName() + "/" + childNode.getServiceName() + "/" + childNode.getEndpointName();
            if (!edgeMap.containsKey(edgeKeyString)){
                edgeMap.put(edgeKeyString, new EndpointEdge(NodeConverter.nodeToEndpoint(node), NodeConverter.nodeToEndpoint(childNode), 1));
            }
            else {
                edgeMap.get(edgeKeyString).incrementCount();
            }

            fillEndpointEdgeHashMapFromNode(childNode, edgeMap);

        });
    }

}
