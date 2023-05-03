# REM-Back-Office

REM-Back-Office is a Java Spring Boot application that manages the survey units repositoryRe for multimode surveys.

## Requirements
* JDK 17 +
* Maven 3.6 +

## Install and excute unit tests
Use the maven clean and maven install 
```shell
mvn clean install
```  

## Running the application locally

### Minimal Local Properties file
Create local.properties in ${HOME}/rem/ directory and complete the following properties:  
```shell  
#Logs configuration
fr.insee.rem.log.file.name=[filepath]
#Application configuration (without auth)
fr.insee.rem.security.auth.mode=noauth
fr.insee.rem.host=http://localhost:8080

#Database configuration
fr.insee.rem.persistence.database.url = 
fr.insee.rem.persistence.database.name = 
fr.insee.rem.persistence.database.user = 
fr.insee.rem.persistence.database.password = 
```
### jar with embedded tomcat

The jar file was created in the application/target directory.
```shell
java -jar application/target/rem-rembo-[version].jar
```  

### Application Accesses locally
To access to swagger-ui, use this url : [http://localhost:8080](http://localhost:8080)  
