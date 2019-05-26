package ITS_MainController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ITS_MainController.DatabaseStubs.ShortestPathForRequest;
import ITS_MainController.DatabaseStubs.ShortestPathForResponse;

@Endpoint
public class PathEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private PathRepository pathRepository;

    @Autowired
    public PathEndpoint(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ShortestPathForRequest")

    @ResponsePayload
    public ShortestPathForResponse getShortestPath(@RequestPayload ShortestPathForRequest request) {
    	ShortestPathForResponse response = new ShortestPathForResponse();
        response.setPath(pathRepository.getShortestPath(request.getName()));

        return response;
    }
}