FROM openjdk:17-oracle

COPY target/*.jar task.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/task.jar"]