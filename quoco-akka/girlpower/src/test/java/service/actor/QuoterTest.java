package service.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.Test;
import service.girlpower.GPQService;
import service.core.ClientInfo;
import service.message.Init;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class QuoterTest {

    @Test
    public void testQuoter() {
        ActorSystem system = ActorSystem.create("girlpower");
        ActorRef quoterRef = system.actorOf(Props.create(Quoter.class), "test");
        TestKit probe = new TestKit(system);
        quoterRef.tell(new Init(new GPQService()), null);
        quoterRef.tell(new QuotationRequest(1,
                        new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1")),
                probe.getRef());
        probe.awaitCond(probe::msgAvailable);
        probe.expectMsgClass(QuotationResponse.class);
    }

}
