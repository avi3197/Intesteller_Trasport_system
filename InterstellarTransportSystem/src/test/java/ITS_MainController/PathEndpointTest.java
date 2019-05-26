package ITS_MainController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ITS_MainController.AllBeans.AllWebService;
import ITS_MainController.AllBeans.DB;
import ITS_MainController.AllBeans.HibernateBean;
import ITS_MainController.Dao.DaoForEdges;
import ITS_MainController.Dao.DaoForTraffics;
import ITS_MainController.Dao.DaoForVertex;
import ITS_MainController.DatabaseStubs.ShortestPathForRequest;
import ITS_MainController.DatabaseStubs.ShortestPathForResponse;
import ITS_MainController.Services.EntityManagerServiceTest;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;


/**
 * Created by Kapeshi.Kongolo on 2016/04/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DB.class, HibernateBean.class, AllWebService.class,
        PathEndpoint.class, PathRepository.class, EntityManagerServiceTest.class, DaoForEdges.class, DaoForVertex.class,
        DaoForTraffics.class},
        loader = AnnotationConfigContextLoader.class)
public class PathEndpointTest {

    @Autowired
    private PathEndpoint PathEndpoint;

    @Test
    public void verifyThatShortestPathSOAPEndPointIsCorrect() throws Exception {
        // Set Up Fixture
        ShortestPathForRequest shortestPathRequest = new ShortestPathForRequest();
        shortestPathRequest.setName("Moon");

        StringBuilder path = new StringBuilder();
        path.append("Earth (A)\tMoon (B)\t");

        ShortestPathForResponse expectedResponse = new ShortestPathForResponse();
        expectedResponse.setPath(path.toString());

        //Test
        ShortestPathForResponse actualResponse = PathEndpoint.getShortestPath(shortestPathRequest);

        // Verify
        assertThat(actualResponse, sameBeanAs(expectedResponse));
        assertThat(actualResponse.getPath(), sameBeanAs("Earth (A)\tMoon (B)\t"));
    }

}