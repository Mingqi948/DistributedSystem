package service.receiver;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.AbstractQuotationService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.QuotationRequestMessage;
import service.message.QuotationResponseMessage;

import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.Queue;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Message;

public class GPQService extends AbstractQuotationService {

    // All references are to be prefixed with an AF (e.g. AF001000)
    public static final String PREFIX = "GP";
    public static final String COMPANY = "Girl Power Ltd.";
    private static GPQService service = new GPQService();

    /**
     * Quote generation:
     * 30% discount for being male
     * 2% discount per year over 60
     * 20% discount for less than 3 penalty points
     * 50% penalty (i.e. reduction in discount) for more than 60 penalty points
     */
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

    public static void main(String[] args) throws Exception {

        String host = args.length > 0 ? args[0] : "localhost";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        factory.setTrustAllPackages(true);
        Connection connection = factory.createConnection();
        connection.setClientID("girlpower");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageProducer producer = session.createProducer(queue);
        MessageConsumer consumer = session.createConsumer(topic);

        connection.start();

        while (true) {
            // Get the next message from the APPLICATION topic
            Message message = consumer.receive();
            // Check it is the right type of message
            if (message instanceof ObjectMessage) {
                // It’s an Object Message
                Object content = ((ObjectMessage) message).getObject();
                if (content instanceof QuotationRequestMessage) {
                    // It’s a Quotation Request Message
                    QuotationRequestMessage request = (QuotationRequestMessage) content;
                    // Generate a quotation and send a quotation response message…
                    Quotation quotation = service.generateQuotation(request.info);
                    Message response = session
                            .createObjectMessage(new QuotationResponseMessage(request.id, quotation));
                    producer.send(response);
                }
            } else {
                System.out.println("Unknown message type: " +
                        message.getClass().getCanonicalName());
            }
        }

    }

}
