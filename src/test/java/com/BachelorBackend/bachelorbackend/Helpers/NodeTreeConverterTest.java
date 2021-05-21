package com.BachelorBackend.bachelorbackend.Helpers;

import com.BachelorBackend.bachelorbackend.models.nodes.Node;
import com.BachelorBackend.bachelorbackend.models.nodes.NodeTree;
import com.BachelorBackend.bachelorbackend.models.responses.Span;
import com.BachelorBackend.bachelorbackend.models.responses.Trace;
import com.BachelorBackend.bachelorbackend.models.responses.TraceEndpoint;
import com.BachelorBackend.bachelorbackend.models.responses.TraceTags;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class NodeTreeConverterTest {
    Trace trace;

    @BeforeEach
    void setUp() {
        List<Span> spans = new ArrayList<>();
        spans.add(generateMockSpan("parent", null, "/offices"));
        spans.add(generateMockSpan("child", "parent", "/offices"));
        spans.add(generateMockSpan("childOfChild", "child", "/user"));
        trace = new Trace(spans);
    }

    private Span generateMockSpan(String id, String parentId, String path) {
        TraceEndpoint localEndpoint = new TraceEndpoint("serviceA", "", "", 8080);
        TraceEndpoint remoteEndpoint = new TraceEndpoint("serviceB", "", "", 8080);
        TraceTags traceTags = new TraceTags("GET", path, "MockController", "getMockMethod", null);
        return new Span(id, "traceId", parentId, "name", new Date().getTime(), 10, "SERVER",
                localEndpoint, remoteEndpoint, traceTags, false, false);
    }

    @Test
    void convertTracesToNodeTrees() {
        ArrayList<Trace> traces = new ArrayList<>();
        traces.add(trace);

        List<Span> spans2 = new ArrayList<>();
        spans2.add(generateMockSpan("parent", null, "/offices"));
        spans2.add(generateMockSpan("child", "parent", "/offices"));
        spans2.add(generateMockSpan(null, "parent", "/tenants"));
        traces.add(new Trace(spans2));

        ArrayList<NodeTree> nodeTree = NodeTreeConverter.convertTracesToNodeTrees(traces);
        // Assert two node trees are created
        Assertions.assertEquals(2, nodeTree.size());
        // Assert the root nodes have different number of children
        Assertions.assertEquals(1, nodeTree.get(0).getRootNode().getChildren().size());
        Assertions.assertEquals(2, nodeTree.get(1).getRootNode().getChildren().size());
        // The first node tree's root node's first child should have it's own child
        Assertions.assertNotNull(nodeTree.get(0).getRootNode().getChildren().get(0));
    }

    @Test
    void convertTracesToNodeTreesEmptyTraces() {
        ArrayList<NodeTree> emptyNodeTree = NodeTreeConverter.convertTracesToNodeTrees(new ArrayList<>());
        Assertions.assertEquals(0, emptyNodeTree.size());
    }

    @Test
    void convertTracesToNodeTreesWhereNoRootSpanIsPresent() {
        ArrayList<Trace> traces = new ArrayList<>();
        List<Span> spans = new ArrayList<>();
        spans.add(generateMockSpan("child", "parent", "/offices"));
        spans.add(generateMockSpan("child", "parent", "/tenants"));
        traces.add(new Trace(spans));

        ArrayList<NodeTree> emptyNodeTree = NodeTreeConverter.convertTracesToNodeTrees(traces);
        Assertions.assertEquals(0, emptyNodeTree.size());
    }

    @Test
    void convertTracesToNodeTreesWhereRootSpanHasError() {
        ArrayList<Trace> traces = new ArrayList<>();
        List<Span> spans = new ArrayList<>();
        Span span = generateMockSpan("parent", null, "/offices");
        span.getTags().setError("404");
        spans.add(span);
        spans.add(generateMockSpan("child", "parent", "/offices"));
        spans.add(generateMockSpan("child", "parent", "/tenants"));
        traces.add(new Trace(spans));

        ArrayList<NodeTree> emptyNodeTree = NodeTreeConverter.convertTracesToNodeTrees(traces);
        Assertions.assertEquals(0, emptyNodeTree.size());
    }

    @Test
    void fillChildren() {
        Node node = new Node("serviceA", "/offices", "parent", new ArrayList<>());
        Assertions.assertEquals(0, node.getChildren().size());

        NodeTreeConverter.fillChildren(node, trace);
        Assertions.assertEquals(1, node.getChildren().size());
        Assertions.assertEquals(1, node.getChildren().get(0).getChildren().size());
    }

    @Test()
    void fillChildrenWhereNodeHasNoListOfChildren() {
        Node node = new Node("serviceA", "/offices", "parent", null);
        Assertions.assertThrows(NullPointerException.class, () -> NodeTreeConverter.fillChildren(node, trace));
    }
}