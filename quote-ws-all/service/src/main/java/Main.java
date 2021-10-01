import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.xml.ws.Endpoint;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import quote.StockQuote;

public class Main {
    public static void main(String[] args) throws Exception {
        // String host = args.length > 0 ? args[0]:"localhost";

        Endpoint endpoint = Endpoint.create(new StockQuote());
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 5);
        server.setExecutor(Executors.newFixedThreadPool(5));
        HttpContext context = server.createContext("/quotation");
        endpoint.publish(context);
        server.start();

        Thread.sleep(8000);

        JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
        // ServiceInfo serviceInfo = ServiceInfo.create(
        //     "_http._tcp.local.", "sqs", 1234, "path=http://"+host+":9000/quotation?wsdl"
        // );
        ServiceInfo serviceInfo = ServiceInfo.create(
            "_http._tcp.local.", "sqs", 9000, "path=/quotation?wsdl"
        );
   }
    
}
