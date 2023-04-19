INSERT INTO usuarios(id, cpf, password, role, nome) values (uuid(), '76491480097', '$2a$12$l3TQKgz7LZdBqOLCVmKSiONorOej8QkOQKSp6B0oU0xjz73ir8R0O', "CLIENTE", 'Gabriel Valmarath');

INSERT INTO usuarios(id, cpf, password, role, nome) values (uuid(), '65000404068', '$2a$12$jbLRMLoYFwLJ7GXFGm6L7.2GbJwclMLmuzApDcyRPGQiY7jRDuip2', "ADMIN", 'Victoria Martello');

INSERT INTO clientes(id, nome, cpf, data_cadastro) values (uuid(), 'Gabriel Valmarath', '76491480097', '2023-04-14');

INSERT INTO contatos(id, cliente, tag, tipo, valor) values (uuid(), (SELECT id FROM clientes WHERE nome = 'Gabriel Valmarath'), 'Gabriel Valmarath', 'e-mail', 'melovalmarath.gabriel@gmail.com');

INSERT INTO enderecos(id, cliente, logradouro, cidade, bairro, complemento, tag) values (uuid(), (SELECT id FROM clientes WHERE nome = 'Gabriel Valmarath'), 'R. Os Dezoito do Forte', 'Caxias do Sul, RS', 'Sao Pelegrino', 'Numero 85', 'Gabriel Valmarath');

INSERT INTO racas(id, descricao) values (uuid(), 'Pitbull');

INSERT INTO pets(id, cliente, raca, data_nascimento, nome) values (uuid(), (SELECT id FROM clientes WHERE nome = 'Gabriel Valmarath'), (SELECT id FROM racas WHERE descricao = 'Pitbull'), '2020-07-25', 'Luna');

INSERT INTO pets(id, cliente, raca, data_nascimento, nome) values (uuid(), (SELECT id FROM clientes WHERE nome = 'Gabriel Valmarath'), (SELECT id FROM racas WHERE descricao = 'Pitbull'), '2020-07-25', 'Scooby');

INSERT INTO atendimentos(id, pet, descricao, valor, data_atendimento) values (uuid(), (SELECT id FROM pets WHERE nome = 'Luna'),'Castracao', '500.00', '2023-02-17');

INSERT INTO atendimentos(id, pet, descricao, valor, data_atendimento) values (uuid(), (SELECT id FROM pets WHERE nome = 'Scooby'),'Exame Periodico', '500.00', '2023-02-17');
