import service.core.BrokerService;
import service.core.Constants;
import service.core.QuotationService;
import service.core.ServerBase;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GPQServer extends ServerBase {

    public static void main(String[] args) {

        try {
            QuotationService gpqService =
                    (QuotationService) UnicastRemoteObject.exportObject(new GPQService(), 0);
            Registry registry = getRegistry(args);
            registry.bind(Constants.GIRL_POWER_SERVICE, gpqService);
            System.out.println("GirlPower server running >>");
            while (true) {Thread.sleep(1000); }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }

    }

}
