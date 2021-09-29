import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.core.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ClientUnitTest {

    private ClientInfo clientInfo = new ClientInfo("Mingqi", 'M', 22, 100, 50, "123456");
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        BrokerService localBrokerService = new LocalBrokerService();
        try {
            //Bind broker service
            registry = LocateRegistry.createRegistry(1099);
            BrokerService brokerService = (BrokerService)
                    UnicastRemoteObject.exportObject(localBrokerService, 0);
            registry.bind(Constants.BROKER_SERVICE, brokerService);

            //Bind AuldFellas service
            QuotationService afqService = (QuotationService)
                    UnicastRemoteObject.exportObject(new AFQService(), 0);
            registry.bind(Constants.AULD_FELLAS_SERVICE, afqService);

            //Bind GirlPower service
            QuotationService gpqService = (QuotationService)
                    UnicastRemoteObject.exportObject(new GPQService(), 0);
            registry.bind(Constants.GIRL_POWER_SERVICE, gpqService);

            //Bind DodgyDrivers service
            QuotationService ddqService = (QuotationService)
                    UnicastRemoteObject.exportObject(new DDQService(), 0);
            registry.bind(Constants.DODGY_DRIVERS_SERVICE, ddqService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void testGetQuotationsWithBrokerService() throws Exception {
        BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        Assert.assertEquals(3, brokerService.getQuotations(clientInfo).size());
    }

    @Test
    public void testDisplayQuotation() throws Exception {
        BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        List<Quotation> quotations = brokerService.getQuotations(clientInfo);
        Client.displayQuotation(quotations.get(0));
    }

    @Test
    public void testDisplayProfile() {
        Client.displayProfile(clientInfo);
    }

}
