# 📚 Biblioteca Personal - Sistema de Gestión de Biblioteca Digital

![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen?style=flat&logo=spring)
![Vaadin](https://img.shields.io/badge/Vaadin-24.9.8-blue?style=flat&logo=vaadin)
![H2 Database](https://img.shields.io/badge/H2-Database-blue?style=flat&logo=h2)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat&logo=apachemaven)

**Biblioteca Personal** es una aplicación web moderna de gestión de biblioteca digital desarrollada con Spring Boot y Vaadin. Permite a los usuarios explorar, gestionar y organizar libros de diferentes categorías de forma intuitiva y segura.

---

## ✨ Características Principales

### 🔐 Sistema de Autenticación y Autorización
- **Registro de usuarios** con validación de datos
- **Login seguro** con Spring Security
- **Autenticación JWT** para protección de endpoints
- **Gestión de sesiones** con cifrado de contraseñas (BCrypt)

### 📖 Catálogo de Libros
- **Biblioteca completa** con más de 55 libros precargados
- **9 categorías**: Novela, Programación, Terror, Ciencia Ficción, Historia, Fantasía, Aventura, Filosofía y Superación Personal
- **Información detallada**: título, autor, portada, páginas y sinopsis
- **Búsqueda y filtrado** por categorías

### 👤 Gestión de Usuarios
- **Panel de administración** para gestión de usuarios
- **Biblioteca personal** ("Mis Libros")
- **Asignación de libros** a usuarios
- **Perfiles personalizados**

### 🎨 Interfaz Moderna
- **Diseño responsivo** con Vaadin
- **Navegación intuitiva** con menú lateral
- **Tarjetas visuales** para cada libro con portadas
- **Feedback visual** con notificaciones

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.5.9** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad y autenticación
- **H2 Database** - Base de datos en memoria
- **JWT** - Tokens de autenticación

### Frontend
- **Vaadin 24.9.8** - Framework UI de Java
- **HTML/CSS** - Estilos personalizados
- **Responsive Design** - Adaptable a todos los dispositivos

### Herramientas de Desarrollo
- **Maven** - Gestión de dependencias
- **Spring Boot DevTools** - Desarrollo con hot-reload
- **Lombok** - Reducción de código boilerplate

---

## 🚀 Instalación y Configuración

### Requisitos Previos
- **JDK 17** o superior
- **Maven 3.6+**
- **IDE** recomendado: IntelliJ IDEA, Eclipse o VS Code

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/jesusgxmez/biblioteca-spring.git
```

2. **Compilar el proyecto**
```bash
mvn clean install
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

4. **Acceder a la aplicación**
```
http://localhost:8080
```

---

## 📋 Configuración de la Base de Datos

### H2 Database (por defecto)
La aplicación utiliza H2 como base de datos en memoria. La configuración se encuentra en `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### Consola H2
Accede a la consola de H2 para inspeccionar la base de datos:
```
http://localhost:8080/h2-console
```
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: *(vacío)*

---

## 👥 Usuarios Precargados

La aplicación incluye usuarios de prueba inicializados automáticamente:

| Usuario | Email | Contraseña | Rol |
|---------|-------|------------|-----|
| test | test@example.com | test | USER |

---

## 📚 Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── controllers/          # Controladores REST
│   │       ├── entities/              # Entidades JPA
│   │       │   ├── CategoriaEsquema.java
│   │       │   ├── LibroEsquema.java
│   │       │   └── UsuarioEsquema.java
│   │       ├── repositories/          # Repositorios JPA
│   │       ├── security/              # Configuración de seguridad
│   │       │   ├── SecurityConfig.java
│   │       │   ├── JwtAuthenticationFilter.java
│   │       │   └── JwtUtil.java
│   │       ├── services/              # Lógica de negocio
│   │       ├── views/                 # Vistas Vaadin
│   │       │   ├── BibliotecaView.java
│   │       │   ├── MisLibrosView.java
│   │       │   ├── LoginView.java
│   │       │   ├── RegistroView.java
│   │       │   ├── UsuarioEsquemaView.java
│   │       │   └── MainLayout.java
│   │       ├── DataInitializer.java   # Datos iniciales
│   │       └── ProyectoJesusApplication.java
│   └── resources/
│       ├── application.properties     # Configuración
│       └── static/                    # Recursos estáticos
└── test/                              # Tests unitarios
```

---

## 🔒 Seguridad

### Autenticación JWT
La aplicación implementa autenticación basada en tokens JWT:

1. **Login**: El usuario envía credenciales y recibe un token JWT
2. **Autorización**: El token se incluye en las peticiones subsiguientes
3. **Validación**: El filtro JWT valida el token en cada request

### Cifrado de Contraseñas
- Las contraseñas se cifran usando **BCrypt** antes de almacenarse
- No se almacenan contraseñas en texto plano

### Endpoints Protegidos
- `/api/**` - Requiere autenticación JWT
- `/biblioteca` - Requiere sesión activa
- `/mis-libros` - Requiere sesión activa
- `/usuarios` - Requiere sesión activa

---

## 🧪 Testing

### Ejecutar Tests
```bash
mvn test
```

### Tests Incluidos
- **Tests de Entidades**: Validación de modelos JPA
- **Tests de Repositorios**: Operaciones CRUD
- **Tests de Servicios**: Lógica de negocio
- **Tests de Seguridad**: Autenticación y autorización

---

## 📖 Uso de la Aplicación

### 1. Registro de Usuario
1. Accede a la aplicación en `http://localhost:8080`
2. Haz clic en "Registrarse"
3. Completa el formulario con:
   - Nombre de usuario
   - Email
   - Contraseña
4. Haz clic en "Registrar"

### 2. Iniciar Sesión
1. Ingresa tu email y contraseña
2. Haz clic en "Iniciar Sesión"
3. Serás redirigido a la Biblioteca

### 3. Explorar la Biblioteca
- **Biblioteca**: Visualiza todos los libros disponibles organizados por categorías
- **Filtrado**: Selecciona una categoría para filtrar libros
- **Detalles**: Cada tarjeta muestra portada, título, autor, páginas y sinopsis

### 4. Gestionar Libros Personales
- **Mis Libros**: Accede a tu biblioteca personal
- **Catálogo**: Los usuarios pueden asignarse libros del catálogo
---

## 📸 Capturas de Pantalla

### Página de Login
<img width="1919" height="936" alt="login" src="https://github.com/user-attachments/assets/34da1473-4f22-414d-925f-c637e67052fe" />

### Catálogo Principal
<img width="1917" height="942" alt="catalogo" src="https://github.com/user-attachments/assets/a579333b-1a26-470d-bbd9-3f9cdc5f8177" />

### Mis Libros
<img width="1919" height="944" alt="mislibros" src="https://github.com/user-attachments/assets/7f9012f5-6428-4552-9766-12c6c57836bd" />


---
## 👨‍💻 Autor

- GitHub: [@jesusgxmez](https://github.com/jesusgxmez)
---
