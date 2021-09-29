import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.core.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.Assert.assertNotNull;

public class LocalBrokerServiceUnitTest {

    private static Registry registry;
    private ClientInfo clientInfo = new ClientInfo("Mingqi", 'M', 22, 100, 50, "123456");

    @BeforeClass
    public static void setup() {
        BrokerService localBrokerService = new LocalBrokerService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            BrokerService brokerService = (BrokerService)
                    UnicastRemoteObject.exportObject(localBrokerService, 0);
            registry.bind(Constants.BROKER_SERVICE, brokerService);
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
    public void testGetQuotationsWithNoService() throws NotBoundException, RemoteException {
        BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        Assert.assertEquals(0, brokerService.getQuotations(clientInfo).size());
    }

    @Test
    public void testGetQuotationsWithOneService() throws Exception {
        BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        QuotationService afqService = (QuotationService)
                UnicastRemoteObject.exportObject(new AFQService(), 0);
        registry.bind(Constants.AULD_FELLAS_SERVICE, afqService);
        Assert.assertEquals(1, brokerService.getQuotations(clientInfo).size());

        registry.unbind(Constants.AULD_FELLAS_SERVICE);
    }

    @Test
    public void testGetQuotationsWithMultipleServices() throws Exception {
        BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

        QuotationService afqService = (QuotationService) UnicastRemoteObject.exportObject(new AFQService(), 0);
        QuotationService gpqService = (QuotationService) UnicastRemoteObject.exportObject(new GPQService(), 0);
        registry.bind(Constants.AULD_FELLAS_SERVICE, afqService);
        registry.bind(Constants.GIRL_POWER_SERVICE, gpqService);

        Assert.assertEquals(2, brokerService.getQuotations(clientInfo).size());

        registry.unbind(Constants.GIRL_POWER_SERVICE);
        registry.unbind(Constants.AULD_FELLAS_SERVICE);
    }


}
