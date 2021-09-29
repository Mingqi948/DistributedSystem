import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import service.core.Constants;
import service.core.QuotationService;
import service.core.ServerBase;

public class AFQServer extends ServerBase {

    public static void main(String[] args) {

        try {
            QuotationService afqService =
                    (QuotationService) UnicastRemoteObject.exportObject(new AFQService(), 0);
            Registry registry = getRegistry(args);
            registry.bind(Constants.AULD_FELLAS_SERVICE, afqService);
            System.out.println("AuldFellas Server running >> ");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

} 