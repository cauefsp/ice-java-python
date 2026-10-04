# ice-java-python

Interface `Printer` da Tarefa ASR 07 ([ice-demo](https://github.com/cauefsp/ice-demo)) com
cliente e servidor em Java e em Python, usando o ZeroC Ice 3.8.

## Como rodar

Requer JDK 17+ e Python 3.12+. No Linux o pip compila o Ice e precisa de `build-essential`,
`python3-dev`, `libssl-dev` e `libbz2-dev`.

```
python3 -m venv .venv
source .venv/bin/activate
pip install zeroc-ice==3.8.3
cd python
slice2py ../slice/Printer.ice
```

Cliente Python e servidor Java:

```
cd java && ./gradlew runServer
cd python && python3 client.py
```

Cliente Java e servidor Python:

```
cd python && python3 server.py
cd java && ./gradlew runClient
```

Os comandos Python precisam do `.venv` ativado. Para rodar em outra máquina:
`python3 client.py <ip-do-servidor>` ou `./gradlew runClient --args="<ip-do-servidor>"`.
