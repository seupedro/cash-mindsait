# Fluxo de Caixa

### Pré-requisitos
- Java 17
- Gradle 7.x
- Docker

Para executar o programa no seu computador, defina o perfil `dev` ou `prod` após a importação local. Por exemplo, esta tarefa pode ser realizada usando o seguinte comando:
```groovy
bootRun --args='--spring.profiles.active=dev'
```

Ou execute o script ./create-containers.sh para levantar todo o ambiente.

## Considerações

### ModelMapper
Para evitar o acoplamento entre as camadas, a biblioteca ModelMapper foi integrada ao projeto. Esta funcionalidade impede que um objeto transite entre múltiplas camadas, evitando, por exemplo, que uma entidade seja diretamente retornada no controlador ou que uma entidade seja modificada a partir do controlador.

### Entidades
As entidades foram projetadas levando em conta vários fatores de segurança padrão, tais como:
- UUID id: proporciona melhor desempenho e previne ataques de varredura de superfície, que poderiam ocorrer em um endpoint GET transaction/{id}, como: 403, 404, 405...
- As operações são entidades financeiras e sua modificação descontrolada ou por alguém não autorizado poderia ser catastrófica. Portanto, para fins de auditoria, as entidades mantêm a data de criação e de atualização.

### Application Properties
Os arquivos application.properties foram configurados para garantir a segurança do projeto quando executado em ambiente de produção, sem sacrificar certas conveniências durante o desenvolvimento.

## Possíveis Aprimoramentos
Com uma disponibilidade maior de tempo para o projeto, algumas melhorias adicionais poderiam ter sido implementadas. As seguintes melhorias estariam sob consideração:
- Esboço gráfico (fluxograma)
- Elaboração de relatórios semanais e mensais
- Melhoria da cobertura de código
- Execução dos testes
- Utilização da biblioteca Cashflow-commons para evitar duplicação de código e bibliotecas
- Implementação do Spring Security com JWT/Oauth
- Uso do Splunk (problema detectado na execução do Splunk devido à incompatibilidade com o Mac M1) https://github.com/splunk/docker-splunk/issues/493
