import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Client {
    public static void main(String[] args) {
        try {
            // Get a reference to the RMI Registry
            Registry registry = LocateRegistry.getRegistry();

            // Find the distributed object (stub created here)
            Calculator c = (Calculator) registry.lookup("Calculator");

            // Do stuff!!!!
            System.out.println(c.sub(4, 3));
            System.out.println(c.add(4, 5));
            System.out.println(c.mul(3, 6));
            System.out.println(c.div(9, 3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
