package com.BachelorBackend.bachelorbackend.Helpers;

import com.BachelorBackend.bachelorbackend.models.nodes.Node;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Span;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;

import java.util.ArrayList;

public class NodeTreeConverter {
    public static ArrayList<NodeTree> convertTracesToNodeTrees(ArrayList<Trace> traces) {
        ArrayList<NodeTree> nodeTrees = new ArrayList<>();
        traces.stream().forEach(trace -> {
            NodeTree nodeTree = new NodeTree();
            //Find the root span, that doesn't have a parent
            Span rootSpan = trace.getSpans().stream().filter(span -> span.getParentId() == null).findFirst().orElse(null);
            if (rootSpan == null) return;

            nodeTree.setRootNode(NodeConverter.convertServerSpanToNode(rootSpan));
            //Fill the tree
            fillChildren(nodeTree.getRootNode(), trace);
            nodeTrees.add(nodeTree);
        });
        return nodeTrees;
    }



    public static void fillChildren(Node node, Trace trace) {
        //Since we can derive everything from the SERVER spans, and we have access to ID's we only need these to fill the tree
        trace.getSpans().stream().filter(span -> (span.getParentId() != null && span.getParentId().equals(node.getId())) && (span.getKind() != null && span.getKind().equals("SERVER"))).forEach(span -> {
                    node.getChildren().add(NodeConverter.convertServerSpanToNode(span));
                }
        );
        node.getChildren().forEach(childNote -> {
            fillChildren(childNote, trace);
        });
    }
}
