FROM eclipse-temurin:25-jre

RUN mkdir /opt/application
COPY *.jar /opt/application/backend.jar

WORKDIR /opt/application

CMD ["java", "-jar", "backend.jar"]