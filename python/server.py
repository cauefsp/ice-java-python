import sys, Ice
import Demo

class PrinterI(Demo.Printer):
    def __init__(self):
        self.historico = []

    def printString(self, s, current=None):
        print(s)
        self.historico.append(s)
        return s + "*"

    def contarPalavras(self, s, current=None):
        return len(s.split())

    def imprimirRepetido(self, s, vezes, current=None):
        if vezes <= 0:
            return ""
        text = " ".join([s] * vezes)
        print(text)
        self.historico.append(text)
        return text

    def obterHistorico(self, current=None):
        return self.historico

communicator = Ice.initialize(sys.argv)

adapter = communicator.createObjectAdapterWithEndpoints("SimpleAdapter", "default -p 5678")
object = PrinterI()
adapter.add(object, Ice.Identity("SimplePrinter"))
adapter.activate()
print("Python server ready on port 5678")

communicator.waitForShutdown()
