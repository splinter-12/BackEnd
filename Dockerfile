FROM openjdk:17-alpine
COPY ./build/libs/*.jar app.jar
CMD ["java","-jar","app.jar"]
EXPOSE 8082
