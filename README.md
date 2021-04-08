# Projeto Guia Bolso - Transação API

API REST do projeto Transação API para aplicação da vaga de Back-end Engineer no Guia Bolso.

## Resumo

O projeto é subdividido em módulos, utiliza arquitetura de API REST e conta com testes automatizados (unitários e de integração). O projeto Guia Bolso - Transação API representa um mock de uma API que devolve informações sobre uma transação, que, ao informar um ID de usuário, retorna uma lista variável de transações contendo a descrição, valor, data (em timestamp) e uma coluna para validação de duplicidade.

### Tecnologias

* **Java 11**
* **Spring Boot 2**
* **Spring Data JPA**
* **PostgreSQL 11**
* **REST API**
* **Testes de Integração e Unitários**
* **JUnit5**
* **Mockito**
* **Banco de dados em memória HSQL para testes de integração**
* **Docker**
* **Docker-compose**

### Arquitetura utilizada e padrões de projeto

* Foi utilizada a Arquitetura Multicamada (Multitier Architecture) para correta separação das camadas de controle, regras de negócio e acesso a dados.

* Foi também utlizada arquitetura REST para a camada de apresentação dos dados.

* Foi utilizado o padrão de projeto DTO (Data Transfer Object) para o retorno das informações aos endpoints, respeitando a premissa de nunca retornar diretamente uma entidade do banco de dados.

* Foi utilizado o padrão de projeto Builder para definição dos objetos criados.

* Foi utilizado o padrão de projeto Repository para definição e separação do acesso ao banco de dados.

* Foi utilizado o padrão de projeto Service para separação das regras de negócio da aplicação, e separando sua interação direta com a camada de persistência de dados.

* Foram utilizados princípios de SOLID, com maior foco no Princípio da Responsabilidade Única (SRP - Single-Responsibility Principle) para a correta separação das responsabilidades de mutação de cada classe, como nas classes de DTOs, models, services e controllers. 

### Por quê utilizei essas tecnologias?

O Spring Boot foi escolhido devido a toda a possibilidade de desenvolvimento escolhido pelo ecossistema Spring, definindo bons padrões para o projeto, facilidade de configuração e desenvolvimento, e sua interação com bibliotecas e classes de testes, e também pela minha experiência de desenvolvimento de APIs e microsserviços com este framework.

## Especificações da API

O endpoint da API é: 

```
GET: /<usuarioId>/transacoes/<ano>/<mes>
```

Em que, os parâmetros podem ser descritos como:

* **usuarioId**: um número *inteiro* entre 1.000 e 100.000.000.
* **ano**: um número *inteiro* entre 1970 e 3000 (como não estava especificado, defini este intervalo para que ficasse em maior conformidade com a realidade).
* **mes**: um número *inteiro* entre 1 e 12.

Possíveis respostas e HTTP Status:

* HTTP Status: OK (200): quando campos informados corretamente, e sendo retornada a lista de informações.
* HTTP Status: BAD REQUEST (400): quando houver qualquer exceção de validação nos dados de entrada.

Possíveis mensagens de erro:

* HTTP Status 400: `O valor do ID do usuário deve estar entre 1.000 e 100.000.000.`. Esta mensagem ocorrerá ao informar um valor menor que 1.000 ou maior que 100.000.000 no parâmetro `usuarioId`.
* HTTP Status 400: `O valor do ano deve estar entre 1970 e 3000.`. Esta mensagem ocorrerá ao informar um valor menor que 1970 ou maior que 3000 no parâmetro `ano`.
* HTTP Status 400: `O valor do mês deve estar entre 1 e 12.`. Esta mensagem ocorrerá ao informar um valor menor que 1 ou maior que 12 no parâmetro `mes`.

Exemplo de **requisição** no formato cURL (utilizando a requisição via Heroku):

```
curl -i -X GET \
 'https://guia-bolso-transacao-api.herokuapp.com/10161/transacoes/2020/1'
```

Exemplo de **resposta de sucesso** (HTTP Status 200):

```
[
    {
        "descricao": "udkdagoovckdnhhkszftcalyefycqvpyafjwndmhqbiwhyaaxtwxh",
        "data": 1578865200000,
        "valor": 6264788,
        "duplicated": false
    },
    {
        "descricao": "emmuqjwwqvpcceoeivaklfyjqmgqnjgymbymcr",
        "data": 1580443620000,
        "valor": 7671217,
        "duplicated": false
    },
    {
        "descricao": "eqngwtczleezottunexizpchrpmdpvnymnahhcvrpawxyukxkasesaie",
        "data": 1579424100000,
        "valor": -8431825,
        "duplicated": false
    },
    {
        "descricao": "hncqvgnfrnsznqqbyeqipmgaoty",
        "data": 1577866980000,
        "valor": -3894015,
        "duplicated": false
    },
    {
        "descricao": "vwohbzjmudmaljmllexbjnynakpwkilqtzvkavbira",
        "data": 1578054840000,
        "valor": -7472188,
        "duplicated": false
    },
    {
        "descricao": "jojwmoehaqfoajnmymp",
        "data": 1580476140000,
        "valor": -5090909,
        "duplicated": false
    },
    {
        "descricao": "sesuyfxppcsibwcywbtkriogdmlmnujgnmhuzeczkveqzjqonnnzi",
        "data": 1578788700000,
        "valor": -9166024,
        "duplicated": false
    },
    {
        "descricao": "liosbhvjxhoafvymlhvswefdoikplpvfewiltvlcikjlamaktinlfgbruj",
        "data": 1578697740000,
        "valor": 1081889,
        "duplicated": false
    },
    ...
]
```

Exemplo de **resposta de erro** (informando o parâmetro `<mes>` como 13, por exemplo):

```
{
    "status": 400,
    "mensagem": "O valor do mês deve estar entre 1 e 12."
}
```

#### Como gerei as transações?

* **Data**: Primeiramente, é gerada uma data de referência com apenas mês e ano no formato YearMonth. Após isso, gero uma data com dias, horas e minutos aleatórios, e converto para o timestamp milissegundos, no formato Long. 

* **Descrição**: É gerado uma String alfanumérica aleatória com tamanhos entre **10** e **60** caracteres.

* **Valor**: É gerado um valor inteiro aleatório com tamanhos entre **-9.999.999** e **9.999.999** caracteres, porém, sempre
tendo um tamanho mínimo de 3 caracteres, pois, se o número gerado for 1, será adicionado 00 em frente, representando os dois valores decimais dos centavos.

* **Duplicated**: Sempre que um conjunto de transações é salvo no banco de dados, há uma verificação para validar se já existe alguma outra transação para aquela mesma data de referência (mês e ano), descricão e valor, se possuir, é feito uma busca por todas as duplicadas, e um é setada como `false`, e as restantes são definidas como `true`. Sempre que as transações são salvas, também é rodada uma validação para verificar se todos os meses do ano que está sendo inserido estão preenchidos com transações, caso todos os meses possuam ao menos uma, são feitas no mínimo três inserções de transações com duplicidade, respeitando a regra de que é necessário posuir duplicidade em no mínimo 3 meses caso todos os meses do ano possuam transações. 

Para especificações mais detalhadas de uso da API, confira abaixo o capítulo sobre a [Documentação](#documentação).

## Especificações técnicas e instalação

### Pré-requisitos

É necessário ter as seguintes ferramentas para inicializar o projeto localmente:

```
Apenas o Docker instalado! ;)
```

Mas, caso queira rodar localmente, executar testes, entre outros, é necessário:

```
Java 11
Gradle 6.6.1
Uma instância do PostgreSQL 11
```

## Execução

Como o projeto utiliza o Docker e o docker-compose, não é necessário instalação manual de dependências.

### Iniciando a aplicação

É possível iniciar a aplicação com Docker, rodando separadamente os dois containers (aplicação e banco de dados), ou utilizando
o docker-compose, e, com apenas um comandinho, tudo estará rodando!

### Executando com **Docker-compose**

Para iniciar os containers, basta rodar na raíz do projeto o comando:

`docker-compose up --build`

Caso não queira visualizar os logs da aplicação e do banco de dados, utilize a flag `-d` ao fim do comando.

#### Executando com **Docker** rodando containers separados

Primeiramente, será necessário criar uma `network`, então vamos definir como:

`docker network create transacao-network`

Primeiramente, vamos criar o container do banco de dados (caso não possua a imagem, o comando `docker run` irá buscá-la!):

```
docker run  \
    --network transacao-network \
    --name transacao-db \
    -e "POSTGRES_PASSWORD=123456" \
    -e "POSTGRES_USER=admin" \
    -e "POSTGRES_DB=transacao" \
    -p 5432:5432 \
    -d \
    postgres:11
```

Agora, iremos realizar a criação da imagem e do container da aplicação transacao-api:

Para criar a imagem:

`docker image build -t transacao-api .`

Para criar e executar o container:

```
docker container run \
    --network transacao-network \
    --name transacao-api  \
    -p 8080:8080 \
    -e "SPRING_PROFILES_ACTIVE=producao" \
    -e "DATABASE_HOST=transacao-db" \
    -e "DATABASE_NAME=transacao" \
    -e "DATABASE_USER=admin" \
    -e "DATABASE_PASSWORD=123456" \
    -d \
    transacao-api
```

Para verificar os logs da aplicação, rode o comando:

`docker logs --follow transacao-api`

E para verificar os logs do banco de dados, rode:

`docker logs --follow transacao-db`

## Testes automatizados (unitários e de integração)

Foram desenvolvidos 35 testes automatizados, 21 deles sendo unitários, testando todas as validações, conversões e métodos únicos das classes e 14 testes de integração no endpoint, verificando todo o fluxo desde o endpoint até a persistência no banco de dados, validando se os dados foram realmente persistidos ou não, caso houvesse alguma entrada de erro.

Para rodar os testes é muito simples, apenas ao executar o build do container eles já serão executados.

Para rodar os testes automatizados manualmente (caso seja sua preferência), será necessário possuir o Gradle instalado. Tendo o Gradle instalado, basta apenas executar:

`gradle build`.

Ou então, é possível rodá-los com o uso de uma IDE, como o IntelliJ IDEA Community (utilizado por mim durante o desenvolvimento deste projeto).

### Cobertura de testes

Utilizando a IDE IntelliJ IDEA da JetBrains, foram rodados os testes unitários e de integração com a opção de coverage para verificar qual foi a cobertura total de testes.

#### Cobertura geral

![Cobertura geral](https://github.com/vhnegrisoli/guia-bolso-transacao-api/blob/master/readme_imagens_gifs/Cobertura%20Geral.png)

A cobertura geral do projeto ficou em 100% de classes testadas (19 de 19 classes), 90% em métodos testados (65 de 72 métodos) e 94% de linhas testadas (203 de 2015).

#### Cobertura de módulos específicos da aplicação

![Cobertura de módulos](https://github.com/vhnegrisoli/guia-bolso-transacao-api/blob/master/readme_imagens_gifs/Cobertura%20M%C3%B3dulos.png)

A aplicação é divida em três módulos principais, o módulo de usuário, transação e comum (contendo constantes, exceptions e classes utilitárias).

O módulo de usuário ficou com 100%"de cobertura de classes (6 de 6 classes testadas), 84% de cobertura de métodos (22 de 26 métodos) e 92% de cobertura de linhas (52 de 56 linhas).

O módulo de usuário ficou com 100%"de cobertura de classes (3 de 3 classes testadas), 100% de cobertura de métodos (23 de 23 métodos) e 95% de cobertura de linhas (80 de 84 linhas).

O módulo comum ficou com 100% de cobertura de classes (4 de 4 classes), 100% de métodos (7 de 7 métodos) e 100% de linhas (40 de 40 linhas).

## Deployment

O deploy da aplicação está sendo realizado no Heroku, e pode ser encontrado na seguinte URL: https://guia-bolso-transacao-api.herokuapp.com/

## Documentação

A API estará sendo documentada com Swagger e poderá ser consultada no endereço:

https://guia-bolso-transacao-api.herokuapp.com/swagger-ui.html

### Endpoint da API

![Documentação do Swagger - Endpoint](https://github.com/vhnegrisoli/guia-bolso-transacao-api/blob/master/readme_imagens_gifs/REST%20Endpoint%20Swagger.png)

### Especificação dos parâmetros e respostas

![Documentação do Swagger - Respostas](https://github.com/vhnegrisoli/guia-bolso-transacao-api/blob/master/readme_imagens_gifs/REST%20Endpoint%20Especifica%C3%A7%C3%B5es.png)

## Autores

* **Victor Hugo Negrisoli** - *Desenvolvedor Back-End* - [vhnegrisoli](https://github.com/vhnegrisoli)
* [![Linkedin Badge](https://img.shields.io/badge/-victorhugonegrisoli-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/victorhugonegrisoli//)](https://www.linkedin.com/in/victorhugonegrisoli/) 
* [![Gmail Badge](https://img.shields.io/badge/-victorhugonegrisoli.ccs@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:sakshamtaneja7861@gmail.com)](mailto:victorhugonegrisoli.ccs@gmail.com)

## Licença

Este projeto possui a licença do MIT. Veja mais em: [LICENSE.txt](LICENSE)

