import service.core.*;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
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


    public static void main(String args[]) {

        try {

            String brokerHost = args.length > 0 ? "http://" + args[0] + ":9000/broker?wsdl" : "http://localhost:9000/broker?wsdl";
            Thread.sleep(10000); //System waits for other 3 quoter services, for docker container case

            //Get urls from broker first
            System.out.println("Connecting to broker at " + brokerHost);
            List<URL> urls = connectToBroker(new URL(brokerHost));

            //Get quotations from each url and for each client
            for(ClientInfo info : clients) {
                displayProfile(info);
                for(URL u : urls) {
                    Quotation quotation = getQuotation(info, u);
                    if(quotation != null) displayQuotation(quotation);
                }
            }

        } catch (Exception e) {
            System.out.println("Trouble: " + e.getMessage());
        }
    }


    /**
     *
     * @param url
     * Connect to service
     */
    private static List<URL> connectToBroker(URL url) {
        List<URL> urls = null;
        try {
            QName serviceName = new QName("http://core.service/", "BrokerService");
            Service service = Service.create(url, serviceName);
            QName portName = new QName("http://core.service/", "BrokerPort");
            BrokerService brokerService = service.getPort(portName, BrokerService.class);;

            //Get url list from broker service
            urls = brokerService.getURLs();

        } catch (WebServiceException e) {
            System.out.println("Broker service is not reachable.");
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    //Get one quotation from a given HOST address
    private static Quotation getQuotation(ClientInfo clientInfo, URL url) {
        //Connect to QuoterService
        Quotation quotation = null;
        try {
            QName serviceName = new QName("http://core.service/", "QuoterService");
            Service service = Service.create(url, serviceName);
            QName portName = new QName("http://core.service/", "QuoterPort");
            QuoterService quotationService = service.getPort(portName, QuoterService.class);
            quotation = quotationService.generateQuotation(clientInfo);
        } catch (Exception e) {
            System.out.println("Can't connect to " + url + " for " + clientInfo.name);
        }
        return quotation;
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
                "| Company: " +  String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
        System.out.println("|=================================================================================================================|");
    }

}
