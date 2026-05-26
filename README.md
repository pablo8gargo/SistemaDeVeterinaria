# Sistema de Veterinaria

Proyecto con backend en Spring Boot y frontend en Next.js, listo para correr con Docker.

## Estructura

- `src/`: backend Java/Spring Boot.
- `frontend/`: aplicacion web Next.js.
- `collections/`: colecciones de Postman para probar la API.

## Requisitos

- Docker Desktop.

## Correr el proyecto

Desde la raiz del proyecto:

```powershell
docker compose up --build
```

Frontend:

```text
http://localhost:3000
```

Backend:

```text
http://localhost:8080/api
```

Consola H2:

```text
http://localhost:8080/api/h2-console
```