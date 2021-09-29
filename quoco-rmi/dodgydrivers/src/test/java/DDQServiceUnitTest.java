import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.core.ClientInfo;
import service.core.Constants;
import service.core.QuotationService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DDQServiceUnitTest {

    private static Registry registry;
    private ClientInfo clientInfo = new ClientInfo("Mingqi", 'M', 22, 100, 50, "123456");

    @BeforeClass
    public static void setup() {
        QuotationService gpqService = new DDQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(gpqService,0);
            registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
                registry.lookup(Constants.DODGY_DRIVERS_SERVICE);
        Assert.assertNotNull(service);
    }

    @Test
    public void generateQuotationTest() throws Exception {
        QuotationService ddqService = new DDQService();
        Assert.assertNotNull(ddqService.generateQuotation(clientInfo));
    }

}

