FROM openjdk:16
RUN mkdir -p /app
COPY ./target/AtyponFinalProject-1.0-SNAPSHOT.jar /app
WORKDIR /app
RUN mv *.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]