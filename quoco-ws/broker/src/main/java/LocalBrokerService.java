import service.core.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class LocalBrokerService implements BrokerService {

    public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {

        Registry registry = LocateRegistry.getRegistry(1099);
        List<Quotation> quotations = new ArrayList<>();

        for(String ref : Constants.getALlCompanies()) {
            try {
                QuotationService service = (QuotationService) registry.lookup(ref);
                quotations.add(service.generateQuotation(info));
            } catch (NotBoundException e) {
                //If a quotation service is currently unavailable as we failed to look up from registry, then print
                //"DodgyDriversService is not available for Donald Duck" etc on broker's console.
                System.out.println(e.getMessage().substring(3) + " is not available for " + info.name);
            }
        }
        return quotations;

    }

}
