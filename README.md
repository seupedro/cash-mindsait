# Cash Flow

### Pré-requisitos
- Java 17
- Gradle 7.x
- Docker

Para executar o programa em sua máquina, especifique o perfil `dev` ou `prod` após a importação local. Por exemplo, você pode realizar essa tarefa utilizando o seguinte comando:
```groovy
bootRun --args='--spring.profiles.active=dev'
```

### Aperfeiçoamentos
Com uma janela de tempo maior disponível para o projeto, algumas melhorias adicionais poderiam ter sido implementadas. As seguintes aperfeiçoamentos estariam em consideração:
- Esbolço gráfico (fluxograma)
- Elaboração de relatórios semanais e mensais
- Melhor cobertura de código
- Execução de testes unitários, integrados e funcionais no módulo Cash-Report
- Uso da biblioteca Cashflow-commons para evitar duplicação de código e bibliotecas
- Implementação do Spring Security com JWT/Oauth
- Uso do Splunk (problema detectado na execução do Splunk devido à incompatibilidade com o Mac M1) https://github.com/splunk/docker-splunk/issues/493

### ModelMapper
Para evitar o acoplamento entre as camadas, a biblioteca ModelMapper foi incorporada ao projeto. Esse recurso impede que um objeto transite por várias camadas, evitando, por exemplo, que uma entidade seja diretamente devolvida no controlador ou que uma entidade seja alterada a partir do controlador.

### Entidades

As entidades foram projetadas considerando diversos fatores de segurança padrão, como:
- UUID id: apresenta melhor performance e previne ataques de varredura de superfície, que poderiam ocorrer em um endpoint GET transaction/{id}, tais como: 403, 404, 405...
- As operações são entidades financeiras e sua alteração sem controle ou por alguém não autorizado poderia ser catastrófico. Por isso, para fins de auditoria, as entidades preservam a data de criação e de atualização.

### Application Properties
Os arquivos application.properties foram configurados para garantir a segurança do projeto quando executado em produção, sem sacrificar algumas conveniências durante o desenvolvimento.
