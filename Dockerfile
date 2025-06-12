FROM bellsoft/liberica-openjre-alpine:21-cds AS layers
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} demo-1.jar.jar
ENTRYPOINT ["java","-jar","/demo-1.jar.jar"]
