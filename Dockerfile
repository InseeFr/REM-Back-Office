# cache as most as possible in this multistage dockerfile.
FROM maven:3.8.4-openjdk-17-slim as DEPS

ARG MAVEN_SETTINGS
COPY $MAVEN_SETTINGS /usr/share/maven/ref/settings.xml

WORKDIR /opt/app
COPY application/pom.xml application/pom.xml
COPY controller/pom.xml controller/pom.xml
COPY domain/pom.xml domain/pom.xml
COPY infrastructure/pom.xml infrastructure/pom.xml


COPY pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

FROM maven:3.8.4-openjdk-17-slim as BUILDER
ENV MAVEN_CONFIG /root/.m2
WORKDIR /opt/app
COPY --from=DEPS /root/.m2 /root/.m2
COPY --from=DEPS /opt/app/ /opt/app
COPY application/src /opt/app/application/src
COPY controller/src /opt/app/controller/src
COPY domain/src /opt/app/domain/src
COPY infrastructure/src /opt/app/infrastructure/src

RUN mvn -B -e clean install -U

FROM openjdk:17
WORKDIR /opt/app
COPY --from=BUILDER /opt/app/application/target/rem-rembo-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD [ "java", "-jar", "/opt/app/application/rem-rembo-0.0.1-SNAPSHOT.jar" ]





