package ITS_MainController.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ITS_MainController.Entity.Edge;
import ITS_MainController.Entity.Traffic;
import ITS_MainController.Entity.Vertex;
import ITS_MainController.Model.ShortestPath;
import ITS_MainController.Services.EntityManagerService;
import ITS_MainController.Services.ShortestPathService;
import ITS_MainController.ValidationStubs.Validation;
import ITS_MainController.ValidationStubs.VertexAndEdgesGraph;

import java.util.LinkedList;
import java.util.List;

@Controller
public class Main_Controller {

    private static final String PATH_NOT_AVAILABLE = "Unavailable.";
    private static final String PATH_NOT_NEEDED = "Not needed. You are already on planet ";
    private static final String NO_PLANET_FOUND = "No planet found.";
    private static final String DUPLICATE_ROUTE = "You cannot link a route to itself.";
    private static final String DUPLICATE_TRAFFIC = "You cannot add traffic on the same route origin and destination.";
    private static final String INVALID_CODE = "Failed to find the validation code. Please start again.";
    private EntityManagerService entityManagerService;
    private ShortestPathService shortestPathService;

    @Autowired
    public Main_Controller(EntityManagerService entityManagerService, ShortestPathService shortestPathService) {
        this.entityManagerService = entityManagerService;
        this.shortestPathService = shortestPathService;
    }

    /*Planets Mapping Start*/

    @RequestMapping(value = "/getAllPlanet", method = RequestMethod.GET)
    public String listVertices(Model model) {
        List allVertices = entityManagerService.getAllVertices();
        model.addAttribute("vertices", allVertices);
        return "vertices";
    }

    @RequestMapping("vertex/{vertexId}")
    public String showVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", entityManagerService.getVertexById(vertexId));
        return "vertexshow";
    }

    @RequestMapping("/addPlanet")
    public String addVertex(Model model) {
        model.addAttribute("vertex", new Vertex());
        return "vertexadd";
    }

    @RequestMapping(value = "vertex", method = RequestMethod.POST)
    public String saveVertex(Vertex vertex, Model model) {
        if (entityManagerService.vertexExist(vertex.getVertexId())) {
            buildVertexValidation(vertex.getVertexId(), model);
            return "validation";
        }
        entityManagerService.saveVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }

    @RequestMapping("vertex/edit/{vertexId}")
    public String editVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", entityManagerService.getVertexById(vertexId));
        return "vertexupdate";
    }

    @RequestMapping(value = "vertexupdate", method = RequestMethod.POST)
    public String updateVertex(Vertex vertex) {
        entityManagerService.updateVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }

    @RequestMapping("vertex/delete/{vertexId}")
    public String deleteVertex(@PathVariable String vertexId) {
        entityManagerService.deleteVertex(vertexId);
        return "redirect:/getAllPlanet";
    }

    public void buildVertexValidation(String vertexId, Model model) {
        String vertexName = entityManagerService.getVertexById(vertexId) == null ? "" : entityManagerService.getVertexById(vertexId).getName();
        String message = "Planet " + vertexId + " already exists as " + vertexName;
        model.addAttribute("validationMessage", message);
    }

    /*Planets Mapping End*/

    /*Routes Mapping Start*/

    @RequestMapping(value = "/getAllRoutes", method = RequestMethod.GET)
    public String listEdges(Model model) {
        List allEdges = entityManagerService.getAllEdges();
        model.addAttribute("edges", allEdges);
        return "edges";
    }

    @RequestMapping("edge/{recordId}")
    public String showEdge(@PathVariable long recordId, Model model) {
        model.addAttribute("edge", entityManagerService.getEdgeById(recordId));
        return "edgeshow";
    }

    @RequestMapping("edge/delete/{recordId}")
    public String deleteEdge(@PathVariable long recordId) {
        entityManagerService.deleteEdge(recordId);
        return "redirect:/getAllRoutes";
    }

    @RequestMapping(value = "/addRoute", method = RequestMethod.GET)
    public String addEdge(Model model) {
        ShortestPath sh = new ShortestPath();
        List allVertices = entityManagerService.getAllVertices();
        model.addAttribute("edge", new Edge());
        model.addAttribute("edgeModel", sh);
        model.addAttribute("routeList", allVertices);
        return "edgeadd";
    }

    @RequestMapping(value = "edge", method = RequestMethod.POST)
    public String saveEdge(Edge edge, @ModelAttribute ShortestPath pathModel, Model model) {
        int id = (int) entityManagerService.getEdgeMaxRecordId() + 1;
        edge.setRecordId(id);
        edge.setEdgeId(String.valueOf(id));
        edge.setSource(pathModel.getSourceVertex());
        edge.setDestination(pathModel.getDestinationVertex());
        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildEdgeValidation(pathModel, model, Validation.ROUTE_TO_SELF.toString());
            return "validation";
        }
        if (entityManagerService.edgeExists(edge)) {
            buildEdgeValidation(pathModel, model, Validation.ROUTE_EXISTS.toString());
            return "validation";
        }
        entityManagerService.saveEdge(edge);
        return "redirect:/edge/" + edge.getRecordId();
    }

    @RequestMapping(value = "edge/edit/{recordId}", method = RequestMethod.GET)
    public String editEdge(@PathVariable long recordId, Model model) {
        ShortestPath pathModel = new ShortestPath();
        List allVertices = entityManagerService.getAllVertices();
        Edge edgeToEdit = entityManagerService.getEdgeById(recordId);
        pathModel.setSourceVertex(edgeToEdit.getSource());
        pathModel.setDestinationVertex(edgeToEdit.getDestination());
        model.addAttribute("edge", edgeToEdit);
        model.addAttribute("edgeModel", pathModel);
        model.addAttribute("routeList", allVertices);
        return "edgeupdate";
    }

    @RequestMapping(value = "edgeupdate", method = RequestMethod.POST)
    public String updateEdge(Edge edge, @ModelAttribute ShortestPath pathModel, Model model) {
        edge.setSource(pathModel.getSourceVertex());
        edge.setDestination(pathModel.getDestinationVertex());
        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildEdgeValidation(pathModel, model, Validation.ROUTE_TO_SELF.toString());
            return "validation";
        }

        if (entityManagerService.edgeExists(edge)) {
            buildEdgeValidation(pathModel, model, Validation.ROUTE_EXISTS.toString());
            return "validation";
        }
        entityManagerService.updateEdge(edge);
        return "redirect:/edge/" + edge.getRecordId();
    }

    public void buildEdgeValidation(@ModelAttribute ShortestPath pathModel, Model model, String code) {
        String message = "";
        Validation mode = Validation.fromString(code);
        if (mode != null) {
            switch (mode) {
                case ROUTE_EXISTS:
                    String sourceName = entityManagerService.getVertexById(pathModel.getSourceVertex()) == null ? "" : entityManagerService.getVertexById(pathModel.getSourceVertex()).getName();
                    String sourceDestination = entityManagerService.getVertexById(pathModel.getDestinationVertex()) == null ? "" : entityManagerService.getVertexById(pathModel.getDestinationVertex()).getName();
                    message = "The route from " + sourceName + " (" + pathModel.getSourceVertex() + ") to " + sourceDestination + "(" + pathModel.getDestinationVertex() + ") exists already.";
                    break;
                case ROUTE_TO_SELF:
                    message = DUPLICATE_ROUTE;
                    break;
                default:
                    message = INVALID_CODE;
                    break;
            }
        }
        //
        model.addAttribute("validationMessage", message);
    }

    /*Routes Mapping End*/

    /*Traffics Mapping Start*/

    @RequestMapping(value = "/getTraffice", method = RequestMethod.GET)
    public String listTraffics(Model model) {
        List<Traffic> allTraffics = entityManagerService.getAllTraffics();
        model.addAttribute("traffics", allTraffics);
        return "traffics";
    }

    @RequestMapping("traffic/{routeId}")
    public String showTraffic(@PathVariable String routeId, Model model) {
        model.addAttribute("traffic", entityManagerService.getTrafficById(routeId));
        return "trafficshow";
    }

    @RequestMapping("traffic/delete/{routeId}")
    public String deleteTraffic(@PathVariable String routeId) {
        entityManagerService.deleteTraffic(routeId);
        return "redirect:/getTraffice";
    }

    @RequestMapping(value = "/addTraffic", method = RequestMethod.GET)
    public String addTraffic(Model model) {
        ShortestPath sh = new ShortestPath();
        List allVertices = entityManagerService.getAllVertices();
        model.addAttribute("traffic", new Traffic());
        model.addAttribute("trafficModel", sh);
        model.addAttribute("trafficList", allVertices);
        return "trafficadd";
    }

    @RequestMapping(value = "traffic", method = RequestMethod.POST)
    public String saveTraffic(Traffic traffic, @ModelAttribute ShortestPath pathModel, Model model) {
        int id = (int) entityManagerService.getTrafficMaxRecordId() + 1;
        traffic.setRouteId(String.valueOf(id));
        traffic.setSource(pathModel.getSourceVertex());
        traffic.setDestination(pathModel.getDestinationVertex());
        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildTrafficValidation(pathModel, model, Validation.TRAFFIC_TO_SELF.toString());
            return "validation";
        }
        if (entityManagerService.trafficExists(traffic)) {
            buildTrafficValidation(pathModel, model, Validation.TRAFFIC_EXISTS.toString());
            return "validation";
        }
        entityManagerService.saveTraffic(traffic);
        return "redirect:/traffic/" + traffic.getRouteId();
    }

    @RequestMapping(value = "traffic/edit/{routeId}", method = RequestMethod.GET)
    public String editTraffic(@PathVariable String routeId, Model model) {
        ShortestPath pathModel = new ShortestPath();
        List allVertices = entityManagerService.getAllVertices();
        Traffic trafficToEdit = entityManagerService.getTrafficById(routeId);
        pathModel.setSourceVertex(trafficToEdit.getSource());
        pathModel.setDestinationVertex(trafficToEdit.getDestination());
        model.addAttribute("traffic", trafficToEdit);
        model.addAttribute("trafficModel", pathModel);
        model.addAttribute("trafficList", allVertices);
        return "trafficupdate";
    }

    @RequestMapping(value = "trafficupdate", method = RequestMethod.POST)
    public String updateTraffic(Traffic traffic, @ModelAttribute ShortestPath pathModel, Model model) {
        traffic.setSource(pathModel.getSourceVertex());
        traffic.setDestination(pathModel.getDestinationVertex());
        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildTrafficValidation(pathModel, model, Validation.TRAFFIC_TO_SELF.toString());
            return "validation";
        }
        if (entityManagerService.trafficExists(traffic)) {
            buildTrafficValidation(pathModel, model, Validation.TRAFFIC_EXISTS.toString());
            return "validation";
        }
        entityManagerService.updateTraffic(traffic);
        return "redirect:/traffic/" + traffic.getRouteId();
    }

    public void buildTrafficValidation(@ModelAttribute ShortestPath pathModel, Model model, String code) {
        String message = "";
        Validation mode = Validation.fromString(code);
        if (mode != null) {
            switch (mode) {
                case TRAFFIC_EXISTS:
                    String sourceName = entityManagerService.getVertexById(pathModel.getSourceVertex()) == null ? "" : entityManagerService.getVertexById(pathModel.getSourceVertex()).getName();
                    String sourceDestination = entityManagerService.getVertexById(pathModel.getDestinationVertex()) == null ? "" : entityManagerService.getVertexById(pathModel.getDestinationVertex()).getName();
                    message = "The traffic from " + sourceName + " (" + pathModel.getSourceVertex() + ") to " + sourceDestination + " (" + pathModel.getDestinationVertex() + ") exists already.";
                    break;
                case TRAFFIC_TO_SELF:
                    message = DUPLICATE_TRAFFIC;
                    break;
                default:
                    message = INVALID_CODE;
                    break;
            }
        }
        //
        model.addAttribute("validationMessage", message);
    }
    /*Traffics Mapping End*/

    /*Shortest Path Mapping Start*/

    @RequestMapping(value = "/getShortestPath", method = RequestMethod.GET)
    public String shortestForm(Model model) {
        ShortestPath pathModel = new ShortestPath();
        List<Vertex> allVertices = entityManagerService.getAllVertices();
        if (allVertices == null || allVertices.isEmpty()) {
            model.addAttribute("validationMessage", NO_PLANET_FOUND);
            return "validation";
        }
        Vertex origin = allVertices.get(0);
        pathModel.setVertexName(origin.getName());
        model.addAttribute("shortest", pathModel);
        model.addAttribute("pathList", allVertices);
        return "shortest";
    }

    @RequestMapping(value = "/getShortestPath", method = RequestMethod.POST)
    public String shortestSubmit(@ModelAttribute ShortestPath pathModel, Model model) {

        StringBuilder path = new StringBuilder();
        VertexAndEdgesGraph graph = entityManagerService.selectGraph();
        if (pathModel.isTrafficAllowed()) {
            graph.setTrafficAllowed(true);
        }
        if (pathModel.isUndirectedGraph()) {
            graph.setUndirectedGraph(true);
        }
        shortestPathService.initializePlanets(graph);
        Vertex source = entityManagerService.getVertexByName(pathModel.getVertexName());
        Vertex destination = entityManagerService.getVertexById(pathModel.getSelectedVertex());
        //
        shortestPathService.run(source);
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);
        if (paths != null) {
            for (Vertex v : paths) {
                path.append(v.getName() + " (" + v.getVertexId() + ")");
                path.append("\t");
            }
        } else if (source != null && destination != null && source.getVertexId().equals(destination.getVertexId())) {
            path.append(PATH_NOT_NEEDED + source.getName());
        } else {
            path.append(PATH_NOT_AVAILABLE);
        }
        pathModel.setThePath(path.toString());
        pathModel.setSelectedVertexName(destination.getName());
        model.addAttribute("shortest", pathModel);
        return "result";
    }

    /*Shortest Path Mapping End*/
}

