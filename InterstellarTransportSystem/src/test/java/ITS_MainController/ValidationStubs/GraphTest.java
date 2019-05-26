package ITS_MainController.ValidationStubs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ITS_MainController.Entity.Edge;
import ITS_MainController.Entity.Traffic;
import ITS_MainController.Entity.Vertex;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertEquals;

/**
 * Created by Kapeshi.Kongolo on 2016/04/18.
 */
public class GraphTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void verifyThatTrafficOverlayOnVertexAndEdgesGraphIsCorrect() throws Exception {
        //Set
        List<Vertex> vertices = new ArrayList<>();

        Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
        List<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
        Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
        Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);

        List<Traffic> traffics = new ArrayList<>();
        traffics.add(traffic1);
        traffics.add(traffic2);
        traffics.add(traffic3);

        Edge edgeExpected1 = new Edge(1, "1", "A", "B", 0.44f, 0.30f);
        Edge edgeExpected2 = new Edge(2, "2", "A", "C", 1.89f, 0.90f);
        Edge edgeExpected3 = new Edge(3, "3", "A", "D", 0.10f, 0.10f);
        List<Edge> edgesExpected = new ArrayList<>();
        edgesExpected.add(edgeExpected1);
        edgesExpected.add(edgeExpected2);
        edgesExpected.add(edgeExpected3);
        boolean expectedTraffic = true;
        VertexAndEdgesGraph expectedVertexAndEdgesGraph = new VertexAndEdgesGraph(vertices, edgesExpected, traffics);
        expectedVertexAndEdgesGraph.setTrafficAllowed(expectedTraffic);


        //Test
        VertexAndEdgesGraph actualVertexAndEdgesGraph = new VertexAndEdgesGraph(vertices, edges, traffics);
        actualVertexAndEdgesGraph.setTrafficAllowed(true);
        actualVertexAndEdgesGraph.processTraffics();
        boolean actualTraffic = actualVertexAndEdgesGraph.isTrafficAllowed();

        List<Vertex> verticesExpected = expectedVertexAndEdgesGraph.getVertexes();
        List<Traffic> trafficsExpected = expectedVertexAndEdgesGraph.getTraffics();
        //Verify
        assertThat(actualVertexAndEdgesGraph, sameBeanAs(expectedVertexAndEdgesGraph));
        assertThat(actualVertexAndEdgesGraph, sameBeanAs(expectedVertexAndEdgesGraph));
        assertThat(vertices, sameBeanAs(verticesExpected));
        assertThat(traffics, sameBeanAs(trafficsExpected));
        assertThat(actualTraffic, sameBeanAs(expectedTraffic));
    }

    @Test
    public void verifyThatUndirectedEdgesOnVertexAndEdgesGraphIsCorrect() throws Exception {
        //Set
        List<Vertex> vertices = new ArrayList<>();

        Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
        List<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
        Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
        Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);

        List<Traffic> traffics = new ArrayList<>();
        traffics.add(traffic1);
        traffics.add(traffic2);
        traffics.add(traffic3);

        boolean expectedUndirected = true;

        //Test
        VertexAndEdgesGraph VertexAndEdgesGraph = new VertexAndEdgesGraph(vertices, edges, traffics);
        VertexAndEdgesGraph.setUndirectedGraph(true);
        List<Edge> actualEdges = VertexAndEdgesGraph.getUndirectedEdges();
        boolean actualUndirected = VertexAndEdgesGraph.isUndirectedGraph();

        VertexAndEdgesGraph actualVertexAndEdgesGraph = new VertexAndEdgesGraph(vertices, actualEdges, traffics);


        Edge edgeExpected1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edgeExpected2 = new Edge(0, "1", "B", "A", 0.44f);
        Edge edgeExpected3 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edgeExpected4 = new Edge(0, "2", "C", "A", 1.89f);
        Edge edgeExpected5 = new Edge(3, "3", "A", "D", 0.10f);
        Edge edgeExpected6 = new Edge(0, "3", "D", "A", 0.10f);
        List<Edge> edgesExpected = new ArrayList<>();
        edgesExpected.add(edgeExpected1);
        edgesExpected.add(edgeExpected2);
        edgesExpected.add(edgeExpected3);
        edgesExpected.add(edgeExpected4);
        edgesExpected.add(edgeExpected5);
        edgesExpected.add(edgeExpected6);

        VertexAndEdgesGraph expectedVertexAndEdgesGraph = new VertexAndEdgesGraph(vertices, edgesExpected, traffics);

        //Verify
        assertThat(actualEdges, sameBeanAs(edgesExpected));
        assertThat(actualVertexAndEdgesGraph, sameBeanAs(expectedVertexAndEdgesGraph));
        assertEquals(actualUndirected, expectedUndirected);
    }

    @Test
    public void verifyThatObjectsAreEqualIsCorrect() throws Exception {
        //Set
        String actualA = "Yes";
        String actualB = "No";
        Object actualObjectA = null;
        Object actualObjectB = null;
        Object actualObjectNotNullA = new Object();
        Object actualObjectNotNullB = new Object();
        Object actualObjectEitherA = null;
        Object actualObjectEitherB = new Object();

        boolean expectedString = false;
        boolean expectedObject = true;
        boolean expectedObjectNotNull = false;
        boolean expectedObjectEither = false;

        VertexAndEdgesGraph VertexAndEdgesGraph = new VertexAndEdgesGraph(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        boolean actualString = VertexAndEdgesGraph.checkObjectsEqual(actualA, actualB);
        boolean actualObject = VertexAndEdgesGraph.checkObjectsEqual(actualObjectB, actualObjectA);
        boolean actualObjectNotNull = VertexAndEdgesGraph.checkObjectsEqual(actualObjectNotNullA, actualObjectNotNullB);
        boolean actualObjectEither = VertexAndEdgesGraph.checkObjectsEqual(actualObjectEitherA, actualObjectEitherB);

        //Verify
        assertEquals(expectedString, actualString);
        assertEquals(expectedObject, actualObject);
        assertEquals(expectedObjectNotNull, actualObjectNotNull);
        assertEquals(actualObjectEither, expectedObjectEither);
    }

}