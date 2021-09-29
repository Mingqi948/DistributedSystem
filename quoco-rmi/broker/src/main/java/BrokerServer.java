import service.core.ServerBase;
import service.core.BrokerService;
import service.core.Constants;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BrokerServer extends ServerBase {

    public static void main(String[] args) {

        try {
            BrokerService brokerService =
                    (BrokerService) UnicastRemoteObject.exportObject(new LocalBrokerService(), 0);
            Registry registry = getRegistry(args);
            registry.bind(Constants.BROKER_SERVICE, brokerService);

            //Print broker server status
            System.out.println("Broker server running");

            //Jam the process
            while (true) {Thread.sleep(1000);}

        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }

    }

}
