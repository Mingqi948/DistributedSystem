import service.core.*;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import java.net.InetAddress;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;

public class Client {

    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
    };

    public static void main(String args[]) throws Exception {
        JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
        jmDNS.addServiceListener("_http._tcp.local.", new WSDLServiceListener());
    }

    public static class WSDLServiceListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println(1);
            String path = event.getInfo().getPropertyString("path");
            if (path != null) {
                String url =event.getInfo().getURLs()[0];
                connectToService(url);
            }
        }
    }


    public static void connectToService(String url) {
        //Connect to service
        try {
            URL wsdlUrl = new URL(url);
            QName serviceName = new QName("http://core.service/", "BrokerService");
            Service service = Service.create(wsdlUrl, serviceName);
            QName portName = new QName("http://core.service/", "BrokerPort");
            BrokerService brokerService = service.getPort(portName, BrokerService.class);
            for (ClientInfo info : clients) {
                displayProfile(info);

                List<Quotation> quotations = brokerService.getQuotations(info);
                for (Quotation q : quotations) {
                    displayQuotation(q);
                }
                System.out.println("\n");
            }
        } catch (WebServiceException e) {
            System.out.println("Broker service is not reachable.");
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the client info nicely.
     *
     * @param info
     */
    public static void displayProfile(ClientInfo info) {
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.name) +
                        " | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
                        " | Age: " + String.format("%1$-30s", info.age)+" |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.licenseNumber) +
                        " | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }
    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
        System.out.println("|=================================================================================================================|");
    }

}
