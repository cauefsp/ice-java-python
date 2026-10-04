# ice-java-python

Trabalho de Sistemas Distribuídos: a interface `Printer` da Tarefa ASR 07
([ice-demo](https://github.com/cauefsp/ice-demo)) com cliente e servidor em Java e em
Python, usando o ZeroC Ice 3.8.

- Cenário 1: cliente Python e servidor Java
- Cenário 2: cliente Java e servidor Python

## Requisitos

- JDK 17 ou mais novo. O Gradle baixa o Ice e o `slice2java`.
- Python 3.12 ou mais novo. No Linux o pip compila o Ice e precisa de `build-essential`,
  `python3-dev`, `libssl-dev` e `libbz2-dev`.

Na raiz do repositório, instale o Ice para Python e gere o código a partir do `Printer.ice`:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install zeroc-ice==3.8.3
cd python
slice2py ../slice/Printer.ice
```

Em cada terminal que rodar o lado Python, ative antes o ambiente com
`source .venv/bin/activate`.

## Cenário 1: cliente Python e servidor Java

```bash
cd java && ./gradlew runServer     # em um terminal
cd python && python3 client.py     # em outro
```

## Cenário 2: cliente Java e servidor Python

```bash
cd python && python3 server.py     # em um terminal
cd java && ./gradlew runClient     # em outro
```

Para o servidor em outra máquina: `python3 client.py <ip-do-servidor>` ou
`./gradlew runClient --args="<ip-do-servidor>"`.

## Middleware x camada de transporte

Nos dois cenários o cliente não sabe em que linguagem o servidor foi escrito. Os dois lados
seguem o mesmo contrato, o `Printer.ice`, e o Ice gera a partir dele o proxy e o esqueleto
de cada linguagem (`slice2java` e `slice2py`). O cliente Python chama
`printer.contarPalavras(...)` do mesmo jeito que chamava o servidor Python na ASR 07, e o
servidor Java só implementa a interface `Printer` gerada. O código Python da ASR 07
praticamente não mudou.

Se fizéssemos isso direto sobre TCP, teríamos que definir o protocolo inteiro na mão:
formato e delimitação das mensagens, como identificar a operação e como representar cada
tipo. No trabalho de cliente-servidor com sockets usamos JSON terminado por `\n`, e deu
certo porque os dois lados eram Python e usavam o mesmo `protocol.py`. Com Java de um lado,
esse protocolo teria que ser reimplementado em Java, e os dois lados teriam que concordar
em detalhes como o tamanho do inteiro (o `int` do Java tem 32 bits e o do Python não tem
limite), a codificação das strings e como uma lista do Python vira um array em Java. Cada
mudança na interface exigiria alterar os dois códigos à mão e mantê-los em sincronia.

Benefícios do middleware neste cenário:

- um contrato único e independente de linguagem, com o código de comunicação gerado
  automaticamente para cada lado;
- o marshalling e o unmarshalling ficam com o Ice, que usa a mesma codificação nas duas
  linguagens: o `Historico` (`sequence<string>`) chega como `list` no Python e como
  `String[]` no Java;
- transparência de acesso: a chamada remota parece uma chamada local, e a localização do
  objeto fica toda na string do proxy (identidade, host e porta);
- erros viram exceções nas duas linguagens (servidor fora do ar, objeto inexistente, erro
  dentro do servidor), em vez de códigos de erro que teríamos que inventar;
- conexões, identificação das requisições e o pool de threads do servidor ficam por conta
  do runtime;
- adicionar uma terceira linguagem seria só gerar o código para ela.

Implicações e custos:

- os dois lados dependem do runtime do Ice em versões compatíveis (aqui, 3.8.3 nos dois).
  A instalação é mais pesada: no Linux o pip compila o Ice, e no Java o `slice2java` vem
  pelo plugin do Gradle;
- há um passo a mais no build, e toda mudança no `.ice` exige gerar o código de novo nas
  duas linguagens;
- perdemos controle sobre o que vai pela rede: o protocolo é binário, tem cabeçalhos
  próprios e é mais difícil de depurar (não dá para testar com `telnet` ou `nc`, como no
  protocolo em texto);
- ficamos acoplados ao middleware: só clientes Ice conversam com o servidor, então um
  navegador ou outro sistema precisaria do Ice ou de um gateway. Também é preciso
  considerar a licença (GPL ou comercial);
- o mapeamento de tipos tem limites: um inteiro do Python que não cabe em 32 bits, passado
  para um `int` do Slice, gera erro na serialização;
- a transparência esconde que a chamada é remota, mas a latência e as falhas de rede
  continuam existindo, e o cliente ainda precisa tratar essas exceções.

Com linguagens diferentes, o middleware compensa: toda a comunicação ficou com o Ice, e o
código de cada lado ficou só com a lógica da aplicação. Construir direto sobre a camada de
transporte faz mais sentido quando precisamos de controle total do protocolo, de overhead
mínimo ou de conversar com sistemas que não usam Ice.
