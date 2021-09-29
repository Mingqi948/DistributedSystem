import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            // Create the RMI Registry
            Registry registry = LocateRegistry.createRegistry(1099);

            // Create the Remote Object
            Calculator c = (Calculator) 
                UnicastRemoteObject.exportObject(new CalculatorImpl(), 0);

            // Register the object with the RMI Registry
            registry.bind("Calculator", c);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
        
        System.out.println("FINISHED");
        new Scanner(System.in).next();
    }
}