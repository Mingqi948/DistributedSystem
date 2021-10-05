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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@WebService
@SOAPBinding(style= SOAPBinding.Style.DOCUMENT, use= SOAPBinding.Use.LITERAL)
public class Broker {

    @WebMethod
    public List<URL> getURLs() {
        System.out.println("Received new request, now processing:");
        return discover();
    }

    private static List<URL> discover() {

        List<URL> urls = new ArrayList();

        try {
            //Discover services
            JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            System.out.println("Discovering services on host: " + jmDNS.getInetAddress().getHostAddress()
                    + ", please wait....");

            //Print out services found/discovered
            String discoveries = "";
            for(ServiceInfo info : jmDNS.list("_http._tcp.local.")) {
                String s = info.getURLs()[0];
                if(s != null && s.length() > 0)
                    discoveries += s + "\n";
                urls.add(new URL(s));
            }
            if(discoveries.length() == 0) System.out.println("No service found!");
            else System.out.println("Services found:\n" + discoveries);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return urls;
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

            //Invoke DNS to discover services
            discover();

            System.out.println("Broker server set up finished!\n----------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
