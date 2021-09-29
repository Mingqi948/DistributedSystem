import service.core.Constants;
import service.core.QuotationService;
import service.core.ServerBase;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DDQServer extends ServerBase {

    public static void main(String[] args) {

        try {
            QuotationService ddqService =
                    (QuotationService) UnicastRemoteObject.exportObject(new DDQService(), 0);
            Registry registry = getRegistry(args);
            registry.bind(Constants.DODGY_DRIVERS_SERVICE, ddqService);
            System.out.println("DodgyDriver Server running >> ");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

} 