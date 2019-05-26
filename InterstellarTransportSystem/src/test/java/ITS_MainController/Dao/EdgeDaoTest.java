package ITS_MainController.Dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ITS_MainController.AllBeans.DB;
import ITS_MainController.AllBeans.HibernateBean;
import ITS_MainController.Entity.Edge;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertEquals;


/**
 * Created by Kapeshi.Kongolo on 2016/04/09.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Edge.class, DaoForEdges.class, DB.class, HibernateBean.class},
        loader = AnnotationConfigContextLoader.class)

public class EdgeDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    private DaoForEdges DaoForEdges;

    @Before
    public void setUp() throws Exception {
        DaoForEdges = new DaoForEdges(sessionFactory);
    }

    @Test
    public void verifyThatSaveEdgeIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(1, "2", "SAVE A", "SAVE B", 2f);
        Edge expectedEdge = new Edge(1, "2", "SAVE A", "SAVE B", 2f);

        //Test
        DaoForEdges.save(edge);
        Edge persistedEdge = DaoForEdges.selectUnique(expectedEdge.getRecordId());

        //Verify
        assertThat(persistedEdge, sameBeanAs(expectedEdge));
        assertEquals("SAVE A", edge.getSource());
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatUpdateEdgeIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(1, "2", "UPDATE A", "UPDATE B", 20f);
        session.save(edge);

        Edge expectedEdge = new Edge(1, "2", "UPDATED A", "UPDATED B", 20f);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(expectedEdge);

        //Test
        DaoForEdges.update(expectedEdge);
        List<Edge> persistedEdges = DaoForEdges.selectAllByRecordId(expectedEdge.getRecordId());

        // Verify
        assertThat(persistedEdges, sameBeanAs(expectedEdges));

        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatDeleteEdgeIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge e1 = new Edge(2, "30", "DELETE A", "DELETE B", 0.17f);
        Edge e2 = new Edge(3, "19", "DELETE C", "DELETE D", 0.19f);
        session.save(e1);
        session.save(e2);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(new Edge(2, "30", "DELETE A", "DELETE B", 0.17f));

        //Test
        DaoForEdges.delete(e2.getRecordId());
        Criteria criteria = session.createCriteria(Edge.class);
        List<Edge> persistedEdges = (List<Edge>) criteria.list();

        // Verify
        assertThat(persistedEdges, sameBeanAs(expectedEdges));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatSelectUniqueEdgeIsCorrect() {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(8, "5", "UNIQUE A", "UNIQUE B", 0.5f);
        Edge expectedEdge = new Edge(9, "7", "UNIQUE C", "UNIQUE D", 0.7f);
        session.save(edge);
        session.save(expectedEdge);

        //Test
        Edge persistedEdge = DaoForEdges.selectUnique(expectedEdge.getRecordId());

        //Verify
        assertThat(persistedEdge, sameBeanAs(expectedEdge));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatSelectAllEdgesByIdIsCorrect() {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge e1 = new Edge(2, "30", "EDGE K", "EDGE F", 0.17f);
        Edge e2 = new Edge(3, "30", "EDGE C", "EDGE D", 0.19f);
        session.save(e1);
        session.save(e2);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(e1);
        expectedEdges.add(e2);

        //Test
        List<Edge> persistedEdge = DaoForEdges.selectAllByEdgeId(e1.getEdgeId());

        //Verify
        assertThat(persistedEdge, sameBeanAs(expectedEdges));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatSelectAllEdgesIsCorrect() {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge e1 = new Edge(2, "30", "ALL K", "ALL F", 0.17f);
        Edge e2 = new Edge(3, "19", "ALL C", "ALL D", 0.19f);
        session.save(e1);
        session.save(e2);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(e1);
        expectedEdges.add(e2);

        //Test
        List<Edge> persistedEdge = DaoForEdges.selectAll();

        //Verify
        assertThat(persistedEdge, sameBeanAs(expectedEdges));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatSelectEdgeMaxRecordIsCorrect() {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge e1 = new Edge(1, "30", "ALL K", "ALL F", 0.17f);
        Edge e2 = new Edge(2, "19", "ALL C", "ALL D", 0.19f);
        session.save(e1);
        session.save(e2);
        long expectedMax = 2;

        //Test
        long returnMax = DaoForEdges.selectMaxRecordId();

        //Verify
        assertThat(returnMax, sameBeanAs(expectedMax));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatEdgeExistsSelectionIsCorrect() {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge e1 = new Edge(1, "1", "A", "B", 0.17f);
        Edge e2 = new Edge(2, "2", "A", "C", 0.19f);
        session.save(e1);
        session.save(e2);

        Edge edgeToCommit = new Edge(3, "3", "A", "C", 3.0f);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(e2);

        //Test
        List<Edge> returnedEdges = DaoForEdges.edgeExists(edgeToCommit);
        //Verify
        assertThat(returnedEdges, sameBeanAs(expectedEdges));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }


}