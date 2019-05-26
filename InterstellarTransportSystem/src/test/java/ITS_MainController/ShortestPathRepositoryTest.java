package ITS_MainController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import ITS_MainController.Services.EntityManagerServiceTest;
import ITS_MainController.AllBeans.DB;
import ITS_MainController.AllBeans.HibernateBean;
import ITS_MainController.Entity.Edge;
import ITS_MainController.Entity.Traffic;
import ITS_MainController.Entity.Vertex;
import ITS_MainController.ValidationStubs.VertexAndEdgesGraph;
import ITS_MainController.Services.EntityManagerService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Kapeshi.Kongolo on 2016/04/20.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DB.class, HibernateBean.class},
        loader = AnnotationConfigContextLoader.class)
public class ShortestPathRepositoryTest {
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager platformTransactionManager;

    @Test
    public void verifyThatDataInitializeAndGiveCorrectPath() throws Exception {

        // SetUp Fixture
        EntityManagerServiceTest entityManagerService = mock(EntityManagerServiceTest.class);

        Vertex vertex1 = new Vertex("A", "Earth");
        Vertex vertex2 = new Vertex("F", "Pluto");
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(vertex1);
        vertices.add(vertex2);
        Edge edge1 = new Edge(1, "30", "A", "F", 0.17f);
        List<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        Traffic traffic = new Traffic("1", "A", "F", 4f);
        List<Traffic> traffics = new ArrayList<>();
        traffics.add(traffic);

        StringBuilder path = new StringBuilder();
        Vertex expectedSource = vertices.get(0);
        Vertex expectedDestination = vertices.get(1);
        VertexAndEdgesGraph graph = new VertexAndEdgesGraph(vertices, edges, traffics);
        LinkedList<Vertex> pathList = new LinkedList<>();
        pathList.add(expectedSource);
        pathList.add(expectedDestination);
        //when(entityManagerService.selectGraph()).thenReturn(graph);
        //when(entityManagerService.getVertexByName(expectedDestination.getName())).thenReturn(expectedDestination);
        //when(entityManagerService.getVertexById(expectedDestination.getVertexId())).thenReturn(expectedDestination);

        path.append("Earth (A)\tPluto (F)\t");
       
        //Verify
    }
}