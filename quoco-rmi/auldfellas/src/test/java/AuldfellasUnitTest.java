import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.core.ClientInfo;
import service.core.Constants;
import service.core.QuotationService;
import org.junit.*;
import static org.junit.Assert.assertNotNull;

public class AuldfellasUnitTest {

    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService afqService = new AFQService();
        try {
            //Bind AuldFellas Service
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(afqService,0);
            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
                registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotationTest() throws Exception {
        ClientInfo clientInfo = new ClientInfo("Mingqi", 'M', 22, 100, 50, "123456");
        QuotationService afqService = new AFQService();
        Assert.assertNotNull(afqService.generateQuotation(clientInfo));
    }

}

