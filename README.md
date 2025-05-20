# 🧩 Spring Boot API - Práctica 2

Este proyecto es una API REST desarrollada con **Spring Boot** como parte de una práctica técnica. Implementa un sistema CRUD con validaciones, manejo de excepciones, seguridad con JWT, documentación con Swagger, conexión a PostgreSQL y cacheo con Redis.

## ✅ Funcionalidades implementadas

### 🛠️ CRUD completo
- Se desarrolló un CRUD completo (Crear, Leer, Actualizar y Eliminar) sobre la entidad principal.
- Implementado mediante un controlador REST (`@RestController`) y servicio con `@Service`.

### 🧪 Validaciones con anotaciones
- Se utilizan anotaciones como:
  - `@NotBlank`
  - `@Email`
  - `@Size`
- Las validaciones se aplican a través de `@Valid` en los controladores.

### ⚠️ Manejo de excepciones personalizado
- Se implementó un manejador global de errores usando `@RestControllerAdvice`.
- Se manejan errores personalizados como:
  - Recurso no encontrado
  - Errores de validación (`MethodArgumentNotValidException`)

### 📖 Documentación con Swagger (OpenAPI)
- Se integró Swagger mediante la dependencia de `springdoc-openapi`.
- Se puede acceder a la documentación en:  
  👉 `http://localhost:8080/swagger-ui.html`

### 🔐 Seguridad con JWT
- Se configuró seguridad con JWT para proteger endpoints.
- Incluye:
  - Filtro de autenticación
  - Filtro de autorización
  - Configuración de seguridad personalizada con `SecurityFilterChain`
- Solo usuarios autenticados pueden acceder a recursos protegidos.

### 🐘 Conexión a PostgreSQL
- Se configuró conexión a base de datos PostgreSQL desde `application.properties`.
- Se utilizó `spring.jpa.hibernate.ddl-auto=update` para facilitar el desarrollo.

### ⚡ Cache con Redis
- Se integró Redis como sistema de cacheo.
- Se cachean las respuestas de ciertos endpoints con `@Cacheable`.
- Redis se configura también desde `application.properties`.

## 🔧 Tecnologías usadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Redis
- Swagger (OpenAPI 3)
- Maven

## 🚀 Instrucciones para correr el proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/IsraelQuenta/Spring_Pratica2.git
