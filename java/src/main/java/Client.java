import Demo.PrinterPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;

public class Client {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";

        try (Communicator communicator = new Communicator(args)) {
            ObjectPrx base = communicator.stringToProxy("SimplePrinter:tcp -h " + host + " -p 5678");
            PrinterPrx printer = PrinterPrx.checkedCast(base);
            if (printer == null) {
                throw new RuntimeException("Invalid proxy");
            }

            printer.printString("Hello World from the Java client!");

            int words = printer.contarPalavras("Remote calls with ICE look like local calls");
            System.out.println("Words: " + words);

            String rep = printer.imprimirRepetido("Hi", 3);
            System.out.println(rep);

            System.out.println("History:");
            for (String line : printer.obterHistorico()) {
                System.out.println(line);
            }
        }
    }
}
