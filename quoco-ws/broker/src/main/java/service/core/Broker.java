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

    //A WSDL web method to return quotations list
    @WebMethod
    public List<Quotation> getQuotations(ClientInfo clientInfo) {

        List<Quotation> quotations = new ArrayList();
        List<URL> urls = new ArrayList<>();

        //Using jmDNS to scan/discover services
        try {
            JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            System.out.println("\nReceived new request & Discovering services on host: "
                    + jmDNS.getInetAddress().getHostAddress()
                    + ", please wait....");
            for(ServiceInfo info : jmDNS.list("_http._tcp.local.")) {
                urls.add(new URL(info.getURLs()[0]));
            }
            if(urls.size() == 0) System.out.println("No service found!");
            else System.out.println("Services found for " + clientInfo.name + ": " + urls + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Connect to quoter services by WSDL interface
        for(URL url : urls) {
            try {
                QName serviceName = new QName("http://core.service/", "QuoterService");
                Service service = Service.create(url, serviceName);
                QName portName = new QName("http://core.service/", "QuoterPort");
                QuoterService quotationService = service.getPort(portName, QuoterService.class);
                quotations.add(quotationService.generateQuotation(clientInfo));
            } catch (Exception e) {
                System.out.println("Can't connect to " + url + " for " + clientInfo.name);
            }
        }
        return quotations;
    }

    public static void main(String[] args) {
        try {

            System.out.println("Initializing broker server...");

            //Publish WSDL web service
            Endpoint endpoint = Endpoint.create(new Broker());
            HttpServer server = HttpServer.create(new InetSocketAddress(Port.BROKER_PORT), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/broker");
            endpoint.publish(context);
            server.start();

            System.out.println("Broker server set up finished!\n----------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
