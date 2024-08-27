•	Instructivo para Desplegar el Proyecto Backend-Mantenedor
1.	Preparar el Entorno.
1.1-	Instalar Java 17: Asegúrate de tener Java 17 instalado en tu sistema. Puedes verificar la instalación con: java -version 
1.2-	Instalar MySQL: Si aún no tienes MySQL instalado, puedes descargarlo e instalarlo desde MySQL Downloads.
1.3-	Abrir visual studio code.

2.	Configurar la Base de Datos.
2.1-Crear la Base de Datos en MySQL: Conéctate a MySQL, crea una base de datos y copia el siguiente script; 
CREATE TABLE tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    vigente BOOLEAN DEFAULT TRUE
);

3.	Configurar la Conexión en application.properties: Configura los detalles de la conexión a MySQL en tu archivo de propiedades: 

3.1-	Properties;
spring.datasource.url=jdbc:mysql://localhost:3306/nombre_de_la_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update

4.	Ejecutar la Aplicación.
Puedes ocupar Maven para ejecutar la aplicación, pero también si te es más cómodo puedes ocupar Spring Boot DevTools, de igual forma te comparto el código para la ejecución con MAVEN:
mvn spring-boot:run

5.	Acceder a la Documentación de la API
Una vez que la aplicación esté en ejecución, puedes acceder a la documentación de la API a través de:
http://localhost:8080/swagger-ui/index.html

6.	Pruebas Unitarias
Para realizar las pruebas Unitarias puedes ejecutar el siguiente código:
mvn test
