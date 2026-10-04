import Demo.Printer;
import com.zeroc.Ice.Current;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrinterI implements Printer {
    private final List<String> historico = new ArrayList<>();

    @Override
    public String printString(String s, Current current) {
        System.out.println(s);
        historico.add(s);
        return s + "*";
    }

    @Override
    public int contarPalavras(String s, Current current) {
        String text = s.trim();
        if (text.isEmpty()) {
            return 0;
        }
        return text.split("\\s+").length;
    }

    @Override
    public String imprimirRepetido(String s, int vezes, Current current) {
        if (vezes <= 0) {
            return "";
        }
        String text = String.join(" ", Collections.nCopies(vezes, s));
        System.out.println(text);
        historico.add(text);
        return text;
    }

    @Override
    public String[] obterHistorico(Current current) {
        return historico.toArray(new String[0]);
    }
}
