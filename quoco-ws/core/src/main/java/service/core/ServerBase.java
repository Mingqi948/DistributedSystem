package service.core;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//An abstract class that supports the other Server object
//To reduce code duplication
public abstract class ServerBase {

    //Method to return Registry depending on port status. Sometimes a port might already in use
    protected static Registry getRegistry(String[] args) throws RemoteException {

        Registry registry;

        if (args.length == 0) {
            try {
                registry = LocateRegistry.createRegistry(1099); //If current port is already in use
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(1099);
            }
        } else {
            registry = LocateRegistry.getRegistry(args[0], 1099);
            System.out.println(args[0] + "----------------");
        }

        return registry;

    }

}
