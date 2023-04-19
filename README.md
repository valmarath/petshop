# Petshop API <img src="https://cdn-icons-png.flaticon.com/512/235/235405.png" width="40" height="40" />

## Descrição do Projeto
Este projeto consiste em uma API HTTP/REST responsável por realizar o sistema de controle de atendimentos de uma petshop. A aplicação foi feita utilizando Spring Boot, com banco de dados MySQL (rodando em um container no Docker) e validação/testes utilizando Postman.
<br/><br/>
### Requisitos obrigatórios

✔️ Banco de dados relacional (MySQL)

✔️ JDK 17 ou superior (utilizado 17)

❌ Testes unitários para demonstrar funcionamento da API

✔️ Autenticação por token JWT

✔️ Autorização role based

✔️ Implementar usando Springboot, Quarkus ou Micronaut (optou-se por spring boot, na versão 3.0.5 e Maven)

✔️ Versionamento GIT, público
<br/><br/>

### Desejável
✔️ Containerização da aplicação (Docker, realizado parcialmente, apenas o banco de dados)

❌ Demais pontos não foram realizados
<br/><br/>

### Pré-Requisitos
- [Docker](https://docs.docker.com/desktop/)
- [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Postman](https://www.postman.com/downloads/) (Ou outra solução equivalente para testar as requisições)

### Como rodar a aplicação?
Faça o download do código fonte da aplicação ou clone o repositório usando o seguinte comando no terminal:
```bash
$ git clone https://github.com/valmarath/petshop.git
``` 
Agora, navegue até o diretório raiz da aplicação no seu terminal ou editor de código e crie/inicie o container do banco de dados:
```bash
$ docker compose up
``` 
Agora, com o banco de dados iniciado (porta 3306), navegue até a pasta src/main e inicie a aplicação(porta 8080). 

### Instruções de uso
Conforme mencionado, para realizar os teste de uso da aplicação recomenda-se a utilização do Postman. A primeira coisa a ser feita é realizar o login. Conforme pode ser observado nos arquivos [data.sql](https://github.com/valmarath/petshop/blob/main/src/main/resources/data.sql) e [schema.sql](https://github.com/valmarath/petshop/blob/main/src/main/resources/schema.sql), o banco de dados já possui alguns registros. Abaixo, há duas opções de login, conforme as roles abaixo:

- CLIENTE ("cpf": "76491480097", "password": "123")
- ADMIN ("cpf": "65000404068", "password": "456")

Com os dados em mãos, deverá ser feita uma requisição do tipo post para a url: http://localhost:8080/login , que retornará um token (JWT) no corpo da resposta. Este token é válido por 60 minutos (1 hora), sendo necessário efetuar a requisição novamente para autenticá-lo. Supondo que seja feito o login com o usuário ADMIN:

<img src="https://github.com/valmarath/petshop/blob/main/prints/login.JPG?raw=true" width="514" height="400" />

Uma ressalva é que há uma validação de CPF no momento do login (e na criação de novos usuários/clientes). Logo, um CPF em formato inadequado irá gerar erro. Novos usuários/clientes não podem ser criados com cpf inválido, visto que passam pela mesma verificação (classe [ValidaCPF.java](https://github.com/valmarath/petshop/blob/main/src/main/java/com/petshop/petshopsystem/ValidaCPF.java )).

Com o login efetuado, é possível começar a utilizar a API, sabendo que antes de executar qualquer requisição deverá ser preenchido o campo authorization (tipo Bearer Token) com o Token recebido previamente.

<img src="https://github.com/valmarath/petshop/blob/main/prints/authorization.JPG?raw=true" width="514" height="400" />

Utilizando o usuário "ADMIN" é possível realizar qualquer requisição (GET, POST, PUT, DELETE) e em qualquer uma das rotas estabelecidas. Usuários "CLIENTE" só podem executar GET e PUT, em rotas especificadas habilitadas para os mesmos e que retornaram ou permitirão apenas a edição de seus dados próprios ou de seus pets. Estas rotas seguem um padrão, mas exemplificando para a tabela de pets:

- http://localhost:8080/pets (GET - Retorna todos os pets) (ADMIN)
- http://localhost:8080/pets-usuario (GET - Retorna pets do usuário logado) (ADMIN e CLIENTE)
- http://localhost:8080/pets/{id} (GET - Retorna pet por id) (ADMIN)
- http://localhost:8080/pets (POST - Cria pet novo) (ADMIN)
- http://localhost:8080/pets/{id} (PUT - Altera pet por id) (ADMIN)
- http://localhost:8080/pets-usuario/{id} (PUT - Altera pet, por id, do usuário logado) (ADMIN e CLIENTE)
- http://localhost:8080/pets (DELETE - Deleta pet) (ADMIN)

O mesmo padrão funciona para as demais tabelas, bastando utilizar seus respectivos nomes (substituindo o "pets" mostrado acima) na url da requisição:

- usuarios
- clientes
- contatos
- enderecos
- atendimentos
- racas

Explicando melhor a role "CLIENTE", suas permissões são:

- usuarios (sem permissão alguma)
- clientes (permissão apenas para GET)
- contatos (permissão para GET e PUT)
- enderecos (permissão para GET e PUT)
- pets (permissão para GET e PUT)
- atendimentos (permissão apenas para GET)
- racas (permissao para GET, na rota /racas)

Uma observação adicional para a criação de certos registros, é que existem algumas dependências. Não é possível acrescentar um dado na tabela de atendimentos, por exemplo, se não existir um pet correspondente na tabela de pets. Da mesma forma, não é possível criar um pet, atendimento ou endereço se não existir um cliente correspondente na tabela de clientes.