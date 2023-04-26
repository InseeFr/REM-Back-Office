# cache as most as possible in this multistage dockerfile.
FROM maven:3.6-alpine as DEPS

WORKDIR /opt/app
COPY application/pom.xml application/pom.xml
COPY controller/pom.xml controller/pom.xml
COPY domain/pom.xml domain/pom.xml
COPY infrastructure/pom.xml infrastructure/pom.xml


COPY pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

# if you have modules that depends each other, you may use -DexcludeArtifactIds as follows
# RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=module1

FROM maven:3.6-alpine as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app/ /opt/app
COPY application/src /opt/app/application/src
COPY controller/src /opt/app/controller/src
COPY domain/src /opt/app/domain/src
COPY infrastructure/src /opt/app/infrastructure/src

# use -o (--offline) if you didn't need to exclude artifacts.
# if you have excluded artifacts, then remove -o flag
RUN mvn -B -e -o clean install -DskipTests=true

# At this point, BUILDER stage should have your .jar or whatever in some path
FROM openjdk:17
WORKDIR /opt/app
COPY --from=builder /opt/app/target/rembo-1.0.0.jar .
EXPOSE 8080
CMD [ "java", "-jar", "/opt/app/rembo-0.0.1-SNAPSHOT.jar" ]
