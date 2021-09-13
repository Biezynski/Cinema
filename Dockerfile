FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
COPY target/Cinema-0.0.1-SNAPSHOT.jar Cinema-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Cinema-0.0.1-SNAPSHOT.jar"]