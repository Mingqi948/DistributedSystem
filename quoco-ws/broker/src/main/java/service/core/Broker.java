package service.core;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@WebService
@SOAPBinding(style= SOAPBinding.Style.DOCUMENT, use= SOAPBinding.Use.LITERAL)
public class Broker {

    @WebMethod
    public List<Quotation> getQuotations(ClientInfo clientInfo) throws Exception {

        return new ArrayList() {{
            getQuotation(clientInfo, "localhost", Port.AULD_FELLAS_PORT);   //Send request to Auldfellas
            getQuotation(clientInfo, "localhost", Port.DODGY_DRIVERS_PORT); //Send request to DodgyDrivers
            getQuotation(clientInfo, "localhost", Port.GIRL_POWER_PORT);    //Send request to GirldPower
        }};

    }

    //Get one quotation from a given HOST address and a given PORT
    private Quotation getQuotation(ClientInfo clientInfo, String host, int port) throws Exception {

        URL wsdlUrl = new URL("http://" + host + ":" + port + "/quotation?wsdl");
        QName serviceName = new QName("http://core.service/", "QuoterService");
        Service service = Service.create(wsdlUrl, serviceName);
        QName portName = new QName("http://core.service/", "QuoterPort");
        QuoterService quotationService = service.getPort(portName, QuoterService.class);

        Quotation quotation = quotationService.generateQuotation(clientInfo);

        return quotation;
    }

    public static void main(String[] args) {
        try {
            Endpoint endpoint = Endpoint.create(new Broker());
            HttpServer server = HttpServer.create(new InetSocketAddress(Port.BROKER_PORT), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/broker");
            endpoint.publish(context);
            server.start();
            System.out.println("Broker server running >>");

            URL wsdlUrl = new URL("http://" + "localhost:" + Port.GIRL_POWER_PORT + "/quotation?wsdl");
            QName serviceName = new QName("http://core.service/", "QuoterService");
            Service service = Service.create(wsdlUrl, serviceName);
            QName portName = new QName("http://core.service/", "QuoterPort");
            QuoterService quotationService = service.getPort(portName, QuoterService.class);
            quotationService.generateQuotation(new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
