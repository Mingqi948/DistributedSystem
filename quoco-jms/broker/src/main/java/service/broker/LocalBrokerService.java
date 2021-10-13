package service.broker;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.Constants;
import service.message.ClientApplicationMessage;
import service.message.QuotationRequestMessage;
import service.message.QuotationResponseMessage;

import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.Queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LocalBrokerService {

    private static Map<Long, ClientApplicationMessage> cache = new HashMap<>();

    public static void main(String[] args) throws Exception {

        String host = args.length > 0 ? args[0] : "localhost";
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        ((ActiveMQConnectionFactory) factory).setTrustAllPackages(true);
        Connection connection = factory.createConnection();
        connection.setClientID("broker");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connection.start();

        Queue quotationsQueue = session.createQueue(Constants.QUOTATIONS_QUEUE);
        Queue requestQueue = session.createQueue(Constants.CLIENT_REQUESTS_QUEUE);
        Topic applicationTopic = session.createTopic(Constants.APPLICATIONS_TOPIC);
        Queue responseQueue = session.createQueue(Constants.CLIENT_RESPONSE_QUEUE);
        MessageConsumer quotationConsumer = session.createConsumer(quotationsQueue);
        MessageConsumer requestConsumer = session.createConsumer(requestQueue);
        MessageProducer applicationProducer = session.createProducer(applicationTopic);
        MessageProducer responseProducer = session.createProducer(responseQueue);

        //Handle incoming requests from Client
        requestConsumer.setMessageListener(message -> {
            try {
                //When receive the request from client, enqueue it to APPLICATIONS topic
                log.info("Received new message, transferring the request to other companies");
                Object content = ((ObjectMessage) message).getObject();
                QuotationRequestMessage request = (QuotationRequestMessage) content;
                cache.put(request.id, new ClientApplicationMessage(request.info, new ArrayList<>()));
                applicationProducer.send(message);
                message.acknowledge();
            } catch (ClassCastException e) {
                log.error("Unknown message type: " + message.getClass().getCanonicalName());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });


        //Handle incoming messages from QUOTATIONS queue, add them to quotations list with relevant id
        quotationConsumer.setMessageListener(message -> {
            try {
                message.acknowledge();
                Object content = ((ObjectMessage) message).getObject();
                QuotationResponseMessage responseMessage = (QuotationResponseMessage) content;
                log.info("Quotation received from " + responseMessage.quotation.company );
                cache.get(responseMessage.id).quotations.add(responseMessage.quotation);

            } catch (ClassCastException e) {
                log.error("Unknown message type: " + message.getClass().getCanonicalName());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });

        //Keep checking whether quotations are ready to be sent back to client
        while (true) {
            Thread.sleep(5000);
            if(cache.size() == 0) continue;
            //Grab results from QUOTATIONS and send the response back to client
            for(ClientApplicationMessage temp : cache.values()) {
                Message response = session.createObjectMessage(temp);
                responseProducer.send(response);
                response.acknowledge();
            }
        }

    }


}
