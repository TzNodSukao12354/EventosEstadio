# Semana 1: Conceptos Básicos de Spring Boot

## ¿Qué aprendimos esta semana?

Esta semana aprendimos qué es Spring Boot y cómo crear un proyecto desde cero.

## ¿Qué es Spring Boot?

Spring Boot es como una caja de herramientas que te ayuda a crear aplicaciones web en Java sin complicarte la vida. Antes, para hacer una aplicación web en Java, tenías que configurar un montón de archivos XML y librerías. Con Spring Boot, todo eso se hace automático.

## ¿Qué hicimos?

### 1. Creamos el proyecto con Spring Initializr

Spring Initializr es como una plantilla que te genera el proyecto base. Es como cuando querés construir una casa y en vez de empezar desde cero, usás un plano ya hecho.

Fuimos a [start.spring.io](https://start.spring.io) y elegimos:
- **Java** como lenguaje
- **Spring Boot 3.x** como versión
- **Maven** como herramienta de construcción

### 2. Elegimos los "Starters" (dependencias)

Los starters son como paquetes de ingredientes. En vez de elegir cada librería por separado, elegís un paquete que ya tiene todo lo que necesitás.

Nuestros starters en `pom.xml`:

| Starter | ¿Para qué sirve? |
|---------|----------------|
| `spring-boot-starter-web` | Para crear endpoints (rutas) que responden a peticiones web |
| `spring-boot-starter-data-jpa` | Para conectarnos a la base de datos |
| `spring-boot-starter-security` | Para proteger nuestra aplicación (login, contraseñas) |
| `spring-boot-starter-test` | Para hacer pruebas unitarias |
| `spring-boot-starter-mail` | Para enviar correos electrónicos |

### 3. El archivo `pom.xml`

El `pom.xml` es como la lista de ingredientes de una receta. Ahí están todas las dependencias (librerías) que nuestro proyecto necesita.

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Más dependencias... -->
</dependencies>
```

## ¿Dónde está el código de esta semana?

- `pom.xml` → La lista de dependencias del proyecto
- `src/main/java/com/example/demo/Main.java` → El archivo principal que arranca la aplicación

## Frase clave para defender

> "Usamos Spring Initializr para configurar los starters necesarios (Web, Data JPA, Security), lo que nos permitió centrarnos en la lógica de negocio del estadio en lugar de la configuración de infraestructura."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Spring Boot | Caja de herramientas para hacer apps web en Java |
| Spring Initializr | Generador de proyectos desde cero |
| Starters | Paquetes de librerías agrupadas |
| pom.xml | Lista de dependencias (ingredientes) |
| Main.java | El archivo que arranca todo |
