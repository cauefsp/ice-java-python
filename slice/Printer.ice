module Demo
{
    sequence<string> Historico;

    interface Printer
    {
        string printString(string s);
        int contarPalavras(string s);
        string imprimirRepetido(string s, int vezes);
        Historico obterHistorico();
    }
}
