import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Quoter;
import service.auldfellas.AFQService;
import service.message.Init;

public class Main {

    public static void main(String[] args) throws Exception {

        Thread.sleep(2000);

        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "auldfellas");
        ref.tell(new Init(new AFQService()), null);

        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        selection.tell("register", ref);
    }

}
