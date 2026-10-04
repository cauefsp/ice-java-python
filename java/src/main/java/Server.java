import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;

public class Server {
    public static void main(String[] args) {
        try (Communicator communicator = new Communicator(args)) {
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("SimpleAdapter", "default -p 5678");
            adapter.add(new PrinterI(), new Identity("SimplePrinter", ""));
            adapter.activate();
            System.out.println("Java server ready on port 5678");

            communicator.waitForShutdown();
        }
    }
}
