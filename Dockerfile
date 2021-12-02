FROM openjdk:11.0
LABEL author="Iwona Adamkiewicz"
ENV PORT=9000
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE $PORT
ENTRYPOINT ["java","-jar","application.jar"]