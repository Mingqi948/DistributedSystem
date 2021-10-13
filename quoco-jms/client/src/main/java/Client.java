import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Constants;
import service.core.Quotation;
import service.message.ClientApplicationMessage;
import service.message.QuotationRequestMessage;

import java.text.NumberFormat;

import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Message;
import javax.jms.ConnectionFactory;



public class Client {

    private static long SEED_ID = 0;

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

        connection.start();

        Queue requestQueue = session.createQueue(Constants.CLIENT_REQUESTS_QUEUE);
        MessageProducer requestProducer = session.createProducer(requestQueue);

        for(ClientInfo info : clients) {
            System.out.println("Sending request to broker for [" + info.name + "]");
            QuotationRequestMessage quotationRequest =
                    new QuotationRequestMessage(SEED_ID++, info);
            Message request = session.createObjectMessage(quotationRequest);
            requestProducer.send(request);

            Queue responseQueue = session.createQueue(Constants.CLIENT_RESPONSE_QUEUE);
            MessageConsumer responseConsumer = session.createConsumer(responseQueue);
            Message message = responseConsumer.receive();
            try {

                Object content = ((ObjectMessage) message).getObject();
                ClientApplicationMessage applicationMessage = (ClientApplicationMessage) content;
                displayProfile(applicationMessage.info);
                for(Quotation quotation : applicationMessage.quotations) {
                    displayQuotation(quotation);
                }
                message.acknowledge();

            } catch (ClassCastException e) {
                System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
            } catch (NullPointerException e) {
                System.out.println("Time out due to the Failure to process " + info.name);
                System.exit(-1);
            }
        }

        connection.close();
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
