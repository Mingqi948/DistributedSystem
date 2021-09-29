import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.core.ClientInfo;
import service.core.Constants;
import service.core.QuotationService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.Assert.assertNotNull;

public class GPQServiceUnitTest {

    private static Registry registry;
    private ClientInfo clientInfo = new ClientInfo("Mingqi", 'M', 22, 100, 50, "123456");

    @BeforeClass
    public static void setup() {
        QuotationService gpqService = new GPQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(gpqService,0);
            registry.bind(Constants.GIRL_POWER_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
                registry.lookup(Constants.GIRL_POWER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotationTest() throws Exception {
        QuotationService gpqService = new GPQService();
        Assert.assertNotNull(gpqService.generateQuotation(clientInfo));
    }

}

