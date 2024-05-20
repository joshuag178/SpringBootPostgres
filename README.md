# Spring Boot con Postgres
* Descargar y configurar Postgres dependiendo del sistema operativo (Definir un usuario y contraseña para acceder a la base de datos):
  * https://www.postgresql.org/download/
* Ejecutar el servidor local de Postgres, verificar que sí se este ejecutando y en qué puerto.
* Crer un nuevo proyecto utilizando Maven en Spring Initilizr con las siguientes dependencias:
  * Spring Web
  * Spring Data JPA
  * PostgreSQL Driver
* Abrir el proyecto en IntelliJ Idea y revisar las dependencias de Maven en el archivo POM.xml
* Agregar las siguientes propiedades en el archivo application.properties reemplazando los valores de <esquema_db>, y <contraseña> respectivamente:
  * spring.datasource.url=jdbc:postgresql://localhost:5433/<esquema_db>
  * spring.datasource.username=<usuario>
  * spring.datasource.password=<contraseña>
  * spring.jpa.hibernate.ddl-auto=update 
  * spring.jpa.show-sql=true 
  * spring.jpa.properties.hibernate.format_sql=true 
  * spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL81Dialect
* Ejecutar el proyecto y verificar que la conexión con la base datos sea exitosa.
* Implementar la siguiente entidad para crear una tabla de usuarios, asegurarse siempre de crear un constructor vacío, requerido para el mapéo de la tabla de la base de datos relacional (en este caso Postgres):
    ```java
  package org.adaschool.datapostgres.data.entity;
    
    import org.adaschool.datapostgres.data.dto.UserDto;
    
    import javax.persistence.*;
    
    @Entity
    @Table(name = "users")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        private String name;
    
        private String email;
    
        public User(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    
        public User() {
        }
    
        public User(UserDto userDto) {
            this.name = userDto.getName();
            this.email = userDto.getEmail();
        }
    
    
        public Long getId() {
            return id;
        }
    
        public String getName() {
            return name;
        }
    
        public String getEmail() {
            return email;
        }
    }
* Crear la interfaz del repositorio que permita acceder la entidad de usuario:
    ```java
    package org.adaschool.datapostgres.data.repository;
  
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.adaschool.datapostgres.data.entity.User;
  
  public interface UserRepository extends JpaRepository<User, Long> {
  }
* Agregar la clase DTO para poder recibir la información de los usuarios desde el Controlador:
    ```java
    package org.adaschool.datapostgres.data.dto;
    
    public class UserDto {
    
        private String name;
    
        private String email;
    
        public UserDto(String name, String email) {
            this.name = name;
            this.email = email;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    }
* Crear una Excepción que pueda ser arrojada cuando un usuario no es econtrado por tu API REST:
  ```java
  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "user not found")
    public class UserNotFoundException extends RuntimeException {
    }
* Implementar el Controlador de usuarios para que se pueda crear y buscar usuarios por ID, inyectando el Repositorio de usuarios por medio del constructor.
  ```java
  import org.springframework.web.bind.annotation.*;
  import org.adaschool.datapostgres.data.entity.User;

  import java.util.Optional;
    
    @RestController
    @RequestMapping("/v1/user")
    public class UserController {
    
        private final UserRepository userRepository;
    
        public UserController(@Autowired UserRepository userRepository) {
            this.userRepository = userRepository;
        }
    
        @PostMapping
        User createUser(@RequestBody UserDto userDto) {
            User user = new User(userDto);
            return userRepository.save(user);
        }
    
        @GetMapping("/{id}")
        User findById(@PathVariable Long id) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent())
                return optionalUser.get();
            else throw new UserNotFoundException();
        }
    } 
* Ejecutar el proyecto y verificar que los dos médotos funcionan (crear y leer usuario)
* Detener la ejecución del proyecto y volver a ejecutarlo, esta vez deberian seguir existiendo los usuarios que se habian creado anteriormente.
* Envíar el enlace del repositorio con la solución.