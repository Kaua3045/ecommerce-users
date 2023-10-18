# Ecommerce Users

## Ferramentas utilizadas
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)

## Projetos complementares do ecommerce
- [Ecommerce](https://github.com/Kaua3045/ecommerce)

## Sobre

Esse projeto está sendo criado para gerenciar os usuários, permissões e roles do meu projeto de e-commerce que será construído no futuro.

- Porquê decidiu fazer esse projeto?
  - Para revizar os conceitos de clean architecture, aprender domain events e aprender a lidar com filas (RabbitMQ).

- Quais foram os desafios de implementá-lo?
  - Implementar a clean architecture e implementar os domain events, garantir que seriam enviados corretamente para a topic e posteriormente enviar para as queues interessadas.

- O que eu aprendi com ele?
  - Aprendi que é extremamente importante salvar os eventos que foram enviados para a topic, pois se o servidor cair, os eventos não serão perdidos e poderão ser enviados novamente para a topic, também aprendi que é importante ter uma fila para cada serviço, pois se um serviço cair, os outros serviços não serão afetados.

## Tabela de conteúdos

- [Arquitetura](#arquitetura)
- [Requsitos para rodar o projeto](#requisitos)
- [Instruções para executar o projeto](#instruções-para-executar-o-projeto)
- [Contribua com o projeto](#contribuindo-com-o-projeto)
- [Changelog](#changelog)

## Arquitetura

![Circulo da clean architecture](doc/imagens/clean-arch-circle)

**Camadas da aplicação**

*Domain, é a camada onde se encontra as regras de negócio, validações e as interfaces gateways (abstração dos métodos do banco dedados, são usadas para remover o acomplamento com o banco de dados)*

*Application, é a camada que contem todos os casos de uso (criar um usuário, pegar um usuário pelo id, atualizar um usuário, deletar um usuário, esse é famoso CRUD) e contem a integração com o gateway do banco de dados*

*Infrastructure, é a camada responsável por conectar tudo, o usuário com a application e domain layer, contem a conexão com o banco de dados, entidades do banco e as rotas*

## Requisitos para rodar o projeto

1. Docker e docker-compose
2. Java e JDK 17

## Instruções para executar o projeto

1. Baixe a aplicação e instale as dependências:
```bash
# Baixando o projeto e acessando o diretorio
git clone https://github.com/Kaua3045/ecommerce-users.git cd ecommerce-users

# Baixando as dependências
./gradlew dependencies  
```

2. Antes de executar a aplicação, você precisa configurar o arquivo .env.example, depois renomeie ele para .env

3. Agora inicie o container do banco de dados e rabbitmq:
```bash
# Execute o container do banco de dados
docker-compose -f docker-compose-dev.yml up -d
```

4. Agora inicie a aplicação:
```bash
# Iniciando a aplicação
./gradlew bootRun
```
5. A url base da aplicação é: *localhost:8080/*

6. Para acessar o swagger da aplicação após o container estar online, acesse: *[Swagger url](http://localhost:8080/swagger-ui/index.html)*

7. Ou então importe a collection do insomnia que esta na pasta [doc/insomnia](doc/insomnia)

## Contribuindo com o projeto

Para contribuir com o projeto, veja mais informações em [CONTRIBUTING](doc/CONTRIBUTING.md)

## Changelog

Para ver as últimas alterações do projeto, acesse [AQUI](doc/changelog.md)

## Configurações para dev
After cloning project add commit-msg hook in your git path
```shell
    git config core.hooksPath .githooks
```
