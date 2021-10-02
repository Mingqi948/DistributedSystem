package service.core;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@WebService
@SOAPBinding(style= SOAPBinding.Style.DOCUMENT, use= SOAPBinding.Use.LITERAL)
public class Broker {

    @WebMethod
    public List<Quotation> getQuotations(ClientInfo clientInfo) {

        List<Quotation> quotations = new ArrayList();
        int[] ports = {9001, 9002, 9003};


        for(int port : ports) {
            try {

                quotations.add(getQuotation(clientInfo, "localhost", port));   //Send request to each port

            } catch (Exception e) {
                String errorMsg = " is currently not online for " + clientInfo.name + "!";
                if(port == Port.AULD_FELLAS_PORT) errorMsg = "AuldFellas server" + errorMsg;
                else if(port == Port.GIRL_POWER_PORT) errorMsg = "GirlPower server" + errorMsg;
                else if(port == Port.DODGY_DRIVERS_PORT) errorMsg = "DodgyDrivers server " + errorMsg;
                else errorMsg = "Invalid input for port number.";
                System.out.println(errorMsg);
            }
        }

        return quotations;
    }

    //Get one quotation from a given HOST address and a given PORT
    private Quotation getQuotation(ClientInfo clientInfo, String host, int port) throws Exception {

        //Connect to service
        try {
            URL wsdlUrl = new URL("http://" + host + ":" + port + "/quotation?wsdl");
            QName serviceName = new QName("http://core.service/", "QuoterService");
            Service service = Service.create(wsdlUrl, serviceName);
            QName portName = new QName("http://core.service/", "QuoterPort");
            QuoterService quotationService = service.getPort(portName, QuoterService.class);
            Quotation quotation = quotationService.generateQuotation(clientInfo);
            System.out.println(clientInfo.name + " got a quotation from: " + quotation.company + " Port: " + port);
            return quotation;
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public static void main(String[] args) {
        try {
            Endpoint endpoint = Endpoint.create(new Broker());
            HttpServer server = HttpServer.create(new InetSocketAddress(Port.BROKER_PORT), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/broker");
            endpoint.publish(context);
            server.start();

            System.out.println("Broker service starts in:");
            for(int i = 5; i >= 0; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
            System.out.println("Broker service is set up >>");

            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "sqs", Port.BROKER_PORT, "path=/broker?wsdl");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
