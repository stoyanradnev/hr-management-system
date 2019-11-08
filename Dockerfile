FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} hr.managementsystem-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/hr.managementsystem-0.0.1-SNAPSHOT.jar"]