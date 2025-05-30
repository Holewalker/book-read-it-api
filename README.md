# BookReadIt-API

API RESTful desarrollada en Spring Boot para la gestión de libros, temas, comentarios, usuarios y notificaciones, pensada para una plataforma tipo foro de lectura colaborativa.

## Características principales

- **Gestión de usuarios:** Registro, login, roles, seguimiento de libros y etiquetas.
- **Libros:** CRUD de libros, búsqueda por título o etiquetas, paginación.
- **Temas:** Creación y gestión de temas asociados a libros.
- **Comentarios:** Comentarios anidados (árbol), notificaciones por participación.
- **Roles:** Asignación de roles OWNER/MODERATOR a usuarios por libro.
- **Notificaciones:** Sistema de notificaciones para usuarios.
- **Seguridad:** Autenticación JWT, roles y permisos.
- **Persistencia:** DynamoDB (AWS) como base de datos principal.

## Endpoints principales

- `/api/auth/*` — Registro y login de usuarios.
- `/api/users/*` — Gestión y consulta de usuarios.
- `/api/book-pages/*` — Gestión y consulta de libros.
- `/api/topics/*` — Gestión y consulta de temas.
- `/api/comments/*` — Gestión y consulta de comentarios.
- `/api/roles/*` — Gestión de roles por libro.
- `/api/notifications/*` — Gestión de notificaciones.
- `/api/tags/*` — Consulta de libros por etiqueta.

## Tecnologías utilizadas

- Java 17
- Spring Boot 2.7.x
- Spring Security (JWT)
- DynamoDB (AWS)
- ModelMapper
- Apache Tika
- Maven
- Docker

## Configuración

1. **Variables de entorno AWS:**  
   Crea un archivo `aws.env` con tus credenciales de AWS:
   ```
   AWS_ACCESS_KEY_ID=TU_ACCESS_KEY
   AWS_SECRET_ACCESS_KEY=TU_SECRET_KEY
   ```

2. **Propiedades de la aplicación:**  
   Configura `src/main/resources/application.properties` y `application-secret.properties` para los endpoints y claves de DynamoDB y JWT.

3. **Compilación y ejecución:**

    Docker:
   ```sh
   docker build -t bookreadit-api .
   docker run -p 8080:8080 --env-file aws.env bookreadit-api
   ```

## Despliegue

Incluye un script `deploy.ps1` para despliegue en Google Cloud Run y soporte para AWS Elastic Beanstalk.

---
