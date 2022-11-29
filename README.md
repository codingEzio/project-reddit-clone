
## Backend

### Foundation

1. Installation
    - Spring Suite (e.g. *Security*, *JPA*, *Web*)
    - Database (e.g. *MySQL Connector*)
    - Auth (e.g. *JSON Web Token*)
    - Dev (e.g. *Lombok*)
    - ..

2. Configuration
    - For account registration, we need a fake *SMTP server*
    - For saving data, we need the database related config

### Preparation

1. *Models*: database *table definition* (with a bit of *validation*)
2. *Repositories*: CRUD functionality based on DB for developers

### Feat :: Account Registration

> A simplified illustration of the process and connection between them
>
> <img src="./doc-images/001-account-registration.jpg" width="350px" height="auto" alt="Illustration of the process of implementing the account registration" />

## Frontend

### Foundation

1. Installation: `npm install -g @angular/cli@9.1.0`
2. Initalization

    ```bash
    ng new reddit-frontend

    cd reddit-frontend
    ng serve
    ```
