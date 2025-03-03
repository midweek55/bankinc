# Bank Inc Cards API

API para la gestión de tarjetas de crédito y transacciones para Bank Inc.

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database (para desarrollo)
- Docker
- Swagger/OpenAPI para documentación

## Requisitos

Para desarrollo local:
- JDK 17+
- Maven 3.6+
- Docker y Docker Compose (opcional)

## Configuración y ejecución

### Ejecución con Java

```bash
mvn clean package
java -jar target/cards-0.0.1-SNAPSHOT.jar
```

### Ejecución con Docker

```bash
docker build -t bankinc-cards .
docker run -p 8080:8080 bankinc-cards
```

### Ejecución con Docker Compose

```bash
docker-compose up
```

## Despliegue en Railway

Este proyecto está configurado para ser desplegado en Railway. Puede desplegarse directamente desde GitHub.

1. Crea una cuenta en [Railway](https://railway.app/)
2. Conecta tu cuenta de GitHub
3. Crea un nuevo proyecto seleccionando este repositorio
4. Railway detectará automáticamente el Dockerfile y desplegará la aplicación

## Variables de entorno

La aplicación soporta las siguientes variables de entorno:

- `PORT`: Puerto en el que se ejecutará la aplicación (por defecto: 8080)
- `DATABASE_URL`: URL de conexión a la base de datos
- `DATABASE_USERNAME`: Usuario de la base de datos
- `DATABASE_PASSWORD`: Contraseña de la base de datos
- `DATABASE_DRIVER`: Driver JDBC para la base de datos
- `DATABASE_DIALECT`: Dialecto de Hibernate para la base de datos
- `LOG_LEVEL`: Nivel de log para el paquete de la aplicación

## Documentación de la API

La documentación de la API está disponible en:

```
http://localhost:8080/swagger-ui.html
```

## Funcionalidades principales

- Generación de tarjetas
- Activación de tarjetas
- Bloqueo de tarjetas
- Recarga de saldo
- Consulta de saldo
- Procesamiento de transacciones
- Anulación de transacciones

## Estructura del proyecto

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bankinc/
│   │   │           └── cards/
│   │   │               ├── config/          # Configuraciones
│   │   │               ├── controller/      # Controladores REST
│   │   │               ├── dto/             # Objetos de transferencia de datos
│   │   │               ├── exception/       # Manejo de excepciones
│   │   │               ├── model/           # Entidades JPA
│   │   │               ├── repository/      # Repositorios JPA
│   │   │               └── service/         # Servicios de negocio
│   │   │
│   │   └── resources/                      # Recursos y configuraciones
│   │
│   └── test/                               # Pruebas unitarias e integración
│
├── docker-compose.yml                      # Configuración de Docker Compose
├── Dockerfile                              # Dockerfile para construir la imagen
├── railway.toml                            # Configuración para Railway
└── pom.xml                                 # Configuración de Maven
```

## Licencia

Este proyecto está licenciado bajo los términos de la licencia MIT.
