# PassCom - Ambiente Docker

Este projeto configura a aplicação PassCom, com backend em Spring Boot e frontend em React com Vite, utilizando Docker e Docker Compose.

## Repositórios
- **Backend**: [PassCom Backend](https://github.com/Nalbertsan/PassCom)
- **Frontend**: [PassCom Frontend](https://github.com/Nalbertsan/passcomfront)

## Configuração com Docker Compose

### Pré-requisitos
- Docker e Docker Compose instalados

## Passo a Passo

### 1. Clone os Repositórios

Clone os repositórios do backend e frontend em diretórios locais.

git clone https://github.com/Nalbertsan/PassCom
git clone https://github.com/Nalbertsan/passcomfront

Crie o arquivo docker-compose.yml

No diretório principal, crie um arquivo docker-compose.yml com o seguinte conteúdo:

```
yaml
Copiar código
version: '3.8'

services:
  app1:  # Primeiro serviço da aplicação Spring
    image: nalbertsan/passcom:1.0
    container_name: passcom-app1
    environment:
      SPRING_APPLICATION_NAME: PassCom1
      SPRING_DATASOURCE_URL: jdbc:postgresql://db1:5432/postgres  # Conecta ao db1
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: 123456
      API_SECURITY_TOKEN_SECRET: 18-08-2022Cal&Nal
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      EXTERNAL_SERVICE_URL: http://app2:8080  # Comunicação com o app2
      SECONDARY_EXTERNAL_SERVICE_URL: http://app3:8080  # Comunicação com o app3
      CITY_ORIGIN: "Bélem"
      CITY_DESTINY: "Fortaleza"
    ports:
      - "9090:8080"

  db1:
    image: postgres:15
    container_name: postgres-db1
    environment:
      POSTGRES_DB: postgres
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 123456
    ports:
      - "5433:5432"
    volumes:
      - postgres-data1:/var/lib/postgresql/data

  app2:
    image: nalbertsan/passcom:1.0
    container_name: passcom-app2
    environment:
      SPRING_APPLICATION_NAME: PassCom2
      SPRING_DATASOURCE_URL: jdbc:postgresql://db2:5432/postgres
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: 123456
      API_SECURITY_TOKEN_SECRET: 18-08-2022Cal&Nal
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      EXTERNAL_SERVICE_URL: http://app1:8080
      SECONDARY_EXTERNAL_SERVICE_URL: http://app3:8080
      CITY_ORIGIN: "Fortaleza"
      CITY_DESTINY: "São Paulo"
    ports:
      - "9091:8080"

  db2:
    image: postgres:15
    container_name: postgres-db2
    environment:
      POSTGRES_DB: postgres
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 123456
    ports:
      - "5434:5432"
    volumes:
      - postgres-data2:/var/lib/postgresql/data

  app3:
    image: nalbertsan/passcom:1.0
    container_name: passcom-app3
    environment:
      SPRING_APPLICATION_NAME: PassCom3
      SPRING_DATASOURCE_URL: jdbc:postgresql://db3:5432/postgres
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: 123456
      API_SECURITY_TOKEN_SECRET: 18-08-2022Cal&Nal
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      EXTERNAL_SERVICE_URL: http://app1:8080
      SECONDARY_EXTERNAL_SERVICE_URL: http://app2:8080
      CITY_ORIGIN: "São Paulo"
      CITY_DESTINY: "Curitiba"
    ports:
      - "9092:8080"

  db3:
    image: postgres:15
    container_name: postgres-db3
    environment:
      POSTGRES_DB: postgres
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 123456
    ports:
      - "5435:5432"
    volumes:
      - postgres-data3:/var/lib/postgresql/data

  frontend:
    image: nalbertsan/react-vite-app:1.0
    container_name: react-vite-frontend
    ports:
      - "5173:80"
    environment:
      VITE_API_BASE_URL: http://app1:8080
      VITE_BASE_API_s1: http://app1:8080
      VITE_BASE_API_s2: http://app2:8080
      VITE_BASE_API_s3: http://app3:8080
    depends_on:
      - app1
      - app2
      - app3

volumes:
  postgres-data1:
  postgres-data2:
  postgres-data3:
```

## Inicie o Docker Compose

No terminal, navegue até o diretório onde o arquivo docker-compose.yml está localizado e execute o comando abaixo para iniciar todos os serviços:

bash
Copiar código
docker-compose up -d
Esse comando irá baixar as imagens, configurar os contêineres e iniciar os serviços em segundo plano.

# Acessando a Aplicação

# Serviço 1: http://localhost:9090
# Serviço 2: http://localhost:9091
# Serviço 3: http://localhost:9092
# Frontend: http://localhost:5173

## Observações
Certifique-se de que as portas mencionadas (9090, 9091, 9092, 5173) estão disponíveis no seu sistema.
Cada instância de banco de dados PostgreSQL e serviço Spring Boot está configurada para se comunicar com as outras.

## Introdução

O setor de aviação de baixo custo enfrenta desafios constantes para otimizar recursos e aumentar a competitividade. A adoção do sistema de venda compartilhada de passagens (PASSCOM) por três companhias aéreas visa melhorar a acessibilidade e maximizar os lucros por meio de uma solução que permite a reserva integrada de trechos de voos entre diferentes empresas. No entanto, cada companhia possui um servidor centralizado e independente, o que torna necessário desenvolver uma comunicação distribuída entre esses servidores. A solução ideal deve garantir que o primeiro cliente a selecionar um trecho tenha preferência e que o sistema seja resiliente a falhas e sem pontos únicos de falha.

Para atender a esses requisitos, este projeto desenvolveu uma aplicação distribuída baseada em contêineres Docker e em uma arquitetura RESTful para comunicação entre os servidores. A solução foi dividida em microserviços, cada um responsável por funções específicas como autenticação e gestão de trechos, assegurando escalabilidade e facilidade de manutenção. Testes de software verificaram a robustez do sistema e demonstraram que a aplicação cumpre com a reserva prioritária e com a segurança das transações. O sistema está pronto para implementação, proporcionando uma integração eficiente entre as LCCs e contribuindo para uma experiência de compra simplificada e confiável para o cliente.

## Especifique os componentes desenvolvidos e seus papeis nessa arquitetura. Como você classificaria a arquitetura usada na solução?

O sistema de venda compartilhada de passagens foi projetado com uma arquitetura distribuída baseada em microserviços. Cada componente foi desenvolvido com um papel específico e encapsulado em contêineres Docker, o que facilita o deploy e a escalabilidade do sistema. Os componentes principais incluem os Controladores, que atuam como intermediários das requisições do cliente e organizam a lógica de negócio; as Classes de Domínio, que representam as entidades centrais, como usuários e trechos de voo; e os Serviços, responsáveis por implementar a lógica para manipulação de dados e comunicação entre servidores. Além disso, foram desenvolvidas diversas classes DTO (Data Transfer Object) para padronizar a troca de informações e assegurar que os dados compartilhados entre os microserviços estejam formatados corretamente.

A comunicação entre os servidores das companhias aéreas foi estruturada por meio de APIs RESTful, garantindo interoperabilidade e evitando bloqueios de firewall. Cada servidor possui suas rotas configuradas para permitir consultas e transações de reserva de trechos, respeitando o princípio de preferência do cliente e assegurando a consistência da venda compartilhada. Também foi incorporado um módulo de Segurança, que utiliza tokens para autenticação, garantindo que apenas usuários autorizados possam realizar transações. O módulo de tratamento de exceções foi implementado para lidar com erros específicos, como trechos já vendidos ou credenciais inválidas, evitando que falhas individuais comprometam o sistema como um todo.

A arquitetura utilizada na solução pode ser classificada como distribuída e orientada a microserviços. Esse modelo proporciona alta modularidade, permitindo que os diferentes componentes operem independentemente e facilitando a manutenção e o escalonamento do sistema conforme necessário. Essa abordagem distribuída, combinada com a flexibilidade dos contêineres Docker, garante resiliência a falhas e uma melhor gestão de recursos entre os servidores, eliminando a necessidade de uma solução centralizada e, assim, mitigando potenciais pontos únicos de falha.

## Especifique as APIs de comunicação implementadas entre os componentes desenvolvidos, descrevendo os métodos remotos, parametros e retornos empregados para permitir a compra de passagens entre clientes e servidores, e entre servidores.

As APIs de comunicação implementadas no sistema permitem que clientes reservem passagens de múltiplas companhias, estabelecendo também uma comunicação direta entre os servidores das LCCs para coordenar a disponibilidade dos trechos. Cada servidor disponibiliza uma série de endpoints RESTful para gerenciar as reservas e responder a consultas de disponibilidade de trechos. Entre os métodos mais importantes, temos o POST /reservarTrecho, que recebe como parâmetros o identificador do trecho, os dados do cliente e o token de autenticação, e verifica se o trecho está disponível para reserva. Caso esteja, o sistema marca o trecho como reservado, retornando um objeto com a confirmação e o código da transação para o cliente.

Além disso, para coordenar a disponibilidade de assentos entre servidores, foram implementados métodos remotos como o GET /consultarDisponibilidade. Esse endpoint recebe o identificador de um trecho de outra companhia e retorna o status de disponibilidade em tempo real. Quando um cliente inicia uma transação envolvendo múltiplas companhias, o servidor consulta a disponibilidade dos trechos adicionais por meio desse método antes de concluir a compra. Outro método importante é o POST /confirmarReserva, que é chamado entre servidores para bloquear temporariamente um assento em um voo, mantendo a preferência do cliente até que ele conclua a compra. Caso a reserva não seja finalizada em um período determinado, o bloqueio é desfeito automaticamente para evitar indisponibilidades indevidas.

Essas APIs são fundamentais para garantir a integridade e a confiabilidade da compra de passagens, seja para clientes ou entre os próprios servidores das companhias. Os parâmetros e retornos padronizados, junto ao uso de tokens de autenticação, asseguram que as transações sejam realizadas com segurança e com os dados necessários para uma resposta ágil e precisa. Com essa estrutura, o sistema viabiliza uma experiência de compra integrada e sem falhas, permitindo que os servidores das LCCs operem de forma sincronizada e transparente para os usuários.

## Qual o método usado para o cálculo distribuído das rotas entre origem e destino da passagem, e se o sistema consegue mostrar para os usuários todas as rotas possíveis considerando os trechos disponíveis nos servidores de todas as companhias.

Para o cálculo das rotas entre a origem e o destino de uma passagem, o sistema utiliza um algoritmo distribuído de busca de caminhos que identifica as conexões disponíveis entre os trechos oferecidos pelas diferentes companhias. Cada servidor armazena a informação sobre seus próprios trechos e comunica essas informações para os demais servidores ao receber uma consulta de rota de um cliente. Quando um cliente solicita uma passagem de um ponto de origem até um destino, o servidor inicial verifica as rotas possíveis internamente e, caso o destino ou algum dos trechos intermediários não estejam disponíveis em sua própria base de dados, ele consulta os servidores das outras companhias para obter as opções adicionais. Esse processo é feito de forma otimizada, garantindo que o cliente visualize as melhores rotas sem um tempo de resposta elevado.

O sistema é capaz de exibir todas as rotas possíveis, considerando os trechos que estão disponíveis em cada um dos servidores das companhias participantes. A partir das respostas obtidas de cada servidor, o sistema compila as rotas viáveis e exibe para o usuário as opções com base na ordem e disponibilidade dos trechos. Dessa forma, o usuário consegue visualizar as combinações de trechos entre as diferentes companhias, sendo possível reservar a rota completa em uma única transação. Essa abordagem distribuída permite que as informações sobre trechos sejam atualizadas em tempo real, garantindo que apenas as rotas realmente disponíveis sejam apresentadas, o que facilita a tomada de decisão por parte do cliente e aumenta a confiabilidade do sistema.

## Especifique conteitualmente a solução empregada para evitar mais vendas de passagens do que a quantidade existente e ou a venda da mesma passagem para clientes distintos.

Para evitar a venda duplicada de passagens e assegurar que a quantidade de assentos disponíveis seja respeitada, o sistema emprega um mecanismo de controle de concorrência com bloqueio de reservas. Esse mecanismo garante que, ao iniciar o processo de reserva de um assento, o trecho seja temporariamente marcado como indisponível para outros usuários até que a transação seja concluída ou cancelada. A cada requisição de reserva, o sistema verifica em tempo real a disponibilidade do assento solicitado e, se o assento estiver disponível, bloqueia-o para impedir que outra transação ocorra sobre ele até o término do processo. Esse bloqueio temporário permite que o primeiro cliente que iniciar a transação tenha a preferência exclusiva sobre o assento, evitando vendas duplicadas.

A implementação desse controle é distribuída, de modo que cada servidor das companhias aéreas gerencia autonomamente a disponibilidade de seus próprios trechos. No caso de uma compra que envolve múltiplos trechos em servidores diferentes, cada servidor participante realiza o bloqueio individualmente para os assentos solicitados, comunicando o status da reserva aos outros servidores envolvidos. Essa estratégia de bloqueio distribuído e temporário é sustentada por uma política de timeout, na qual o sistema desfaz o bloqueio se a transação não for concluída em um tempo predefinido. Essa abordagem assegura que trechos não sejam reservados indefinidamente, o que poderia prejudicar a disponibilidade para outros clientes, mantendo o fluxo de vendas eficiente e com o menor tempo de espera possível.

Adicionalmente, o sistema adota um protocolo de comunicação seguro entre os servidores para sincronizar a disponibilidade dos assentos em tempo real. Cada reserva é acompanhada de uma confirmação transacional que atualiza imediatamente o estado do assento em todos os servidores participantes, garantindo consistência e evitando conflitos. Essa solução conceitual de controle de concorrência distribuída permite uma experiência de compra sem conflitos, em que o cliente que conclui primeiro a transação tem a garantia de preferência, enquanto os demais clientes são redirecionados para alternativas viáveis em caso de indisponibilidade. Esse método proporciona uma segurança robusta ao processo de reserva e previne problemas de overbooking ou venda indevida de passagens.

## Desconectando e conectando os servidores das companhias, o sistema continua garantindo a concorrência distribuída e a finalização da compra anteriormente iniciada por um cliente.

O sistema foi projetado para garantir a continuidade e a consistência das transações mesmo quando um servidor de companhia aérea se desconecta temporariamente. Para isso, ele implementa um mecanismo de tolerância a falhas que armazena de forma temporária os estados de transação de compra em andamento. Quando um cliente inicia uma compra que envolve múltiplos servidores, o servidor principal registra os trechos bloqueados de cada servidor e mantém essas informações durante todo o processo da transação. Dessa forma, mesmo que um dos servidores de uma companhia se desconecte momentaneamente, a reserva de seus trechos continua válida e bloqueada para o cliente até que a transação seja concluída ou expire o tempo de espera predefinido. Assim, ao reconectar, o servidor retoma a sincronização com o estado atualizado das reservas em andamento.

Esse controle distribuído também permite que, no caso de falha temporária de um servidor, o cliente não perca o progresso da compra. O servidor que inicia a transação pode comunicar o status de cada trecho bloqueado aos demais servidores, assegurando que, ao final da compra, todos os trechos estejam devidamente confirmados. Caso o cliente conclua a compra enquanto um servidor está desconectado, a transação será mantida em estado de “pendente” para esse servidor até sua reconexão, quando então o servidor confirmará o status final da reserva. Esse mecanismo garante que a compra seja finalizada com sucesso para o cliente, mesmo em um ambiente de conectividade variável entre servidores, preservando a concorrência distribuída e a integridade das transações em tempo real.

## Se foi desenvolvido e mantido no Github o código para testar a consistência da solução sob condições críticas e ou avaliar o desempenho do sistema.

Sim, todo o codigo da aplicaçação foi mantido no github e foi desenvolvido um conjunto de testes automatizados que tambem foram mantidos no repositório do GitHub da equipe para validar a consistência e desempenho da solução sob condições críticas. Esses testes foram projetados para simular cenários de alta concorrência, onde múltiplos clientes tentam reservar o mesmo trecho de passagem ao mesmo tempo, e para avaliar a robustez da arquitetura distribuída quando há desconexão ou falhas temporárias entre os servidores. Utilizando ferramentas de teste de carga, o sistema foi submetido a diferentes níveis de estresse para identificar possíveis gargalos e assegurar que o mecanismo de bloqueio de trechos funcionasse corretamente, evitando vendas duplicadas e respeitando o princípio de preferência do cliente.

Além de validar a consistência, os testes no GitHub incluem métricas de desempenho, como o tempo de resposta das APIs e a eficiência do sistema ao reconectar servidores em transações incompletas. Essas métricas foram registradas e analisadas para otimizar a resposta do sistema em situações de grande demanda e de comunicação intermitente. A documentação dos testes foi mantida no GitHub, com instruções para que a equipe e futuros desenvolvedores possam replicar e verificar a eficácia do sistema em cenários críticos, assegurando a confiabilidade e estabilidade contínua da solução.

## Se o código do projeto possui comentários explicando as principais classes, e se as funções têm descrições sobre seu propósito, parâmetros, e o retorno esperado.

O código do projeto foi desenvolvido com um cuidado especial para incluir comentários explicativos nas principais classes, oferecendo uma visão clara da estrutura e das responsabilidades de cada componente. Cada controlador — como o AccentController, AuthController, e TravelController — possui comentários detalhados descrevendo seu papel e a interação com o sistema. As classes de domínio, como Accent, Travel, e User, também contam com documentação que explica sua finalidade, atributos principais e como elas se integram no fluxo da aplicação. Esses comentários são fundamentais para ajudar desenvolvedores futuros a entenderem rapidamente a estrutura do projeto, tornando mais fácil a manutenção e possíveis expansões no futuro.

As funções estão documentadas com descrições sobre o propósito de cada método, incluindo explicações dos parâmetros recebidos e o tipo de retorno esperado. Por exemplo, métodos de serviço como AccentService e UserService detalham os processos internos de cada função, garantindo que a lógica implementada para gerenciar as transações e a segurança esteja bem descrita. Esses comentários não apenas facilitam a compreensão do fluxo de dados e a lógica do código, mas também oferecem uma camada extra de transparência no que diz respeito às expectativas de entrada e saída, permitindo que a equipe de desenvolvimento e stakeholders compreendam as funcionalidades sem a necessidade de análises extensivas linha por linha.

## Explique como o projeto fez o emprego da tecnologia Docker para contaneirizar os serviços e testes implementados.

O projeto utilizou a tecnologia Docker para contêinerizar seus serviços, garantindo que cada componente do sistema fosse isolado em contêineres independentes, facilitando o gerenciamento e a escalabilidade. No arquivo Dockerfile, configuramos o ambiente de cada serviço, incluindo as dependências necessárias e as configurações de rede, assegurando que cada servidor ou serviço dentro do sistema de venda compartilhada de passagens possa ser executado de forma autônoma. Com o uso do docker-compose.yml, organizamos múltiplos contêineres, permitindo que cada serviço de servidor, cliente e os componentes auxiliares interajam em uma rede privada, replicando o ambiente de produção e facilitando o fluxo de comunicação REST entre os servidores das LCCs.

A contêinerização também incluiu um ambiente para testes, possibilitando que as validações do software ocorressem sem interferir no ambiente principal de desenvolvimento. Com Docker, os testes podem ser realizados em um ambiente idêntico ao de produção, o que aumenta a confiabilidade e a consistência dos resultados. Dessa forma, a equipe de desenvolvimento pôde configurar testes para verificar a integridade de transações, a segurança de dados e a comunicação entre os contêineres sem comprometer a estabilidade do sistema principal. Além disso, a possibilidade de escalar rapidamente qualquer componente contêinerizado permitiu uma validação ágil do sistema, com a realização de testes paralelos que aumentaram a eficiência do processo de desenvolvimento e validação da solução final.
