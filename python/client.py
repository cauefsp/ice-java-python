import sys, Ice
import Demo

communicator = Ice.initialize(sys.argv)

host = sys.argv[1] if len(sys.argv) > 1 else "localhost"
base = communicator.stringToProxy("SimplePrinter:tcp -h " + host + " -p 5678")
printer = Demo.PrinterPrx.checkedCast(base)
if not printer:
    raise RuntimeError("Invalid proxy")

printer.printString("Hello World from the Python client!")

words = printer.contarPalavras("Remote calls with ICE look like local calls")
print("Words:", words)

rep = printer.imprimirRepetido("Hi", 3)
print(rep)

print("History:")
for line in printer.obterHistorico():
    print(line)

communicator.destroy()
