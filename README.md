# Board — Gerenciamento de Quadros, Colunas e Cards

Projeto backend em Java desenvolvido como **projeto guiado** durante o **Bootcamp CI&T**, oferecido pela **Digital Innovation One (DIO)**.

O projeto tem caráter educacional e foi utilizado para praticar conceitos de Java, organização em camadas, persistência de dados e versionamento com Git.

---

## 📱 Aplicação

A aplicação consiste em um sistema de gerenciamento de boards no estilo Kanban, executado em **modo console**.

Por meio de um menu interativo no terminal, é possível:
- Criar e visualizar boards
- Gerenciar colunas
- Criar, mover, bloquear, desbloquear e finalizar cards
- Persistir os dados em banco de dados relacional

Toda a interação com o usuário acontece via terminal, sem interface gráfica ou API REST.

---

## 🎓 Contexto do Projeto

Este projeto foi desenvolvido **integralmente como um projeto guiado**, seguindo o escopo e as instruções propostas no **Bootcamp CI&T (DIO)**.

O foco principal foi:
- Acompanhar a implementação passo a passo
- Compreender a organização do código
- Entender a separação entre camadas
- Praticar versionamento com Git em um projeto real

Não houve criação de funcionalidades além do que foi solicitado no projeto original.

---

## 🏗️ Estrutura do Projeto

O projeto segue a estrutura definida no guia do bootcamp, organizada em camadas:

- **UI (Console)**  
  Responsável pela interação com o usuário via menus no terminal

- **Service**  
  Camada que concentra a lógica definida no projeto

- **DAO**  
  Responsável pelo acesso ao banco de dados

- **Entity**  
  Representação das entidades persistidas

- **DTO**  
  Objetos utilizados para transporte de dados

- **Exception**  
  Exceções utilizadas conforme o fluxo definido no projeto

---

## 🗄️ Banco de Dados

- Banco de dados relacional **MySQL**
- Migrations gerenciadas com **Liquibase**
- Estrutura criada conforme o roteiro do projeto

---

## 🐳 Docker

Como adaptação prática pessoal, o projeto foi executado utilizando **Docker e Docker Compose** para subir o banco de dados MySQL, facilitando o ambiente de desenvolvimento.

Essa foi a principal customização em relação ao guia original.

---

## ▶️ Como executar a aplicação

### Pré-requisitos
- Docker
- Docker Compose
- Java 17+

### Passos

1. Subir o banco de dados:
```bash
docker-compose up -d

### Executar a aplicação:

./gradlew run

👨‍💻 Autor

Gustavo Batista

Projeto desenvolvido como projeto guiado no Bootcamp CI&T (DIO), com foco em aprendizado prático.
