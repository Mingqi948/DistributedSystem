package service.broker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class LocalBrokerService {

    private Map<Long, ClientApplication> cache = new HashMap();

    //Return a ClientApplicationResponse Object
    @PostMapping("/application")
    public ClientApplicationResponse getQuotations(@RequestBody ClientApplication request) {

        List<Quotation> quotations = new ArrayList<>();

        //Add quotation to quotation list
        for(String endpoint : Constants.endpoints) {
            try {
                quotations.add(getQuotation(request.getInfo(), endpoint));
            } catch (Exception e) {
                log.error(endpoint + " is not reachable at the moment.");
            }
        }

        ClientApplicationResponse response = new ClientApplicationResponse(request.getInfo(), quotations);
        cache.put(request.getApplicationID(), request);
        log.info(String.format("Stored application ID: %d to cache.", request.getApplicationID()));

        return response;
    }

    //Get an application by id
    @GetMapping("/application/{id}")
    public ClientApplication getResponseByID(@PathVariable("id") long id) {
        log.info("Fetch application by ID = " + id);
        return cache.get(id);
    }

    //Get all ClientApplication objects
    @GetMapping("/applications")
    public List<ClientApplication> getCache() {
        List<ClientApplication> applications = new ArrayList<>();
        for(ClientApplication application : cache.values()) {
            applications.add(application);
        }
        log.info("applications request approved");
        return applications;
    }

    //Get one quotation from given endpoint, using rest template
    private Quotation getQuotation(ClientInfo info, String endpoint) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ClientInfo> request = new HttpEntity<>(info);
        Quotation quotation = restTemplate.postForObject(endpoint,
                request, Quotation.class);
        return quotation;
    }

}
