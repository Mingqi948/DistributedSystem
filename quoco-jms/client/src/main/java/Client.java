import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.QuotationRequestMessage;
import service.message.QuotationResponseMessage;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import javax.jms.*;


public class Client {

    private static long SEED_ID = 0;
    private static Map<Long, ClientInfo> cache = new HashMap<>();

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

        String host = args.length > 0 ? args[0] : "localhost";
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        ((ActiveMQConnectionFactory) factory).setTrustAllPackages(true);
        Connection connection = factory.createConnection();
        connection.setClientID("client");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageConsumer consumer = session.createConsumer(queue);
        MessageProducer producer = session.createProducer(topic);

        connection.start();

        QuotationRequestMessage quotationRequest =
                new QuotationRequestMessage(SEED_ID++, clients[0]);
        Message request = session.createObjectMessage(quotationRequest);
        cache.put(quotationRequest.id, quotationRequest.info);
        producer.send(request);

        Message message = consumer.receive();
        if (message instanceof ObjectMessage) {
            Object content = ((ObjectMessage) message).getObject();
            if (content instanceof QuotationResponseMessage) {
                QuotationResponseMessage response = (QuotationResponseMessage) content;
                ClientInfo info = cache.get(response.id);
                displayProfile(info);
                displayQuotation(response.quotation);
                System.out.println("\n");
            }
            message.acknowledge();
        } else {
            System.out.println("Unknown message type: " +
                    message.getClass().getCanonicalName());
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
                "| Company: " +  String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
        System.out.println("|=================================================================================================================|");
    }

}
