package service.core;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Implementation of the AuldFellas insurance quotation service.
 *
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style= SOAPBinding.Style.RPC, use= SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService{
    // All references are to be prefixed with an AF (e.g. AF001000)
    public static final String PREFIX = "DD";
    public static final String COMPANY = "Dodgy Drivers Ltd.";

    /**
     * Quote generation:
     * 30% discount for being male
     * 2% discount per year over 60
     * 20% discount for less than 3 penalty points
     * 50% penalty (i.e. reduction in discount) for more than 60 penalty points
     */
    @WebMethod
    public Quotation generateQuotation(ClientInfo info) {
        // Create an initial quotation between 600 and 1200
        double price = generatePrice(600, 600);

        // Automatic 30% discount for being male
        int discount = (info.gender == ClientInfo.MALE) ? 30:0;

        // Automatic 2% discount per year over 60...
        discount += (info.age > 60) ? (2*(info.age-60)) : 0;

        // Add a points discount
        discount += getPointsDiscount(info);

        // Generate the quotation and send it back
        return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
    }

    private int getPointsDiscount(ClientInfo info) {
        if (info.points < 3) return 20;
        if (info.points <= 6) return 0;
        return -50;

    }

    public static void main(String[] args) {
        try {
            String hostConfig = args.length > 0 ? "path=" + args[0] : "path=http://localhost:9003/quotation?wsdl";

            //Publish WSDL service
            Endpoint endpoint = Endpoint.create(new Quoter());
            HttpServer server = HttpServer.create(new InetSocketAddress(Port.DODGY_DRIVERS_PORT), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/quotation");
            endpoint.publish(context);
            server.start();

            //Register service to DNS
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "sqs", Port.DODGY_DRIVERS_PORT, hostConfig);
            jmdns.registerService(serviceInfo);

            System.out.println(String.format("DodgyDrivers server running at %s >>", jmdns.getInetAddress().getHostAddress()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}