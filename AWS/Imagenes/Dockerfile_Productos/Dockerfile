FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y openjdk-17-jre
RUN apt-get install -y openjdk-17-jdk
COPY Productos.jar /home/Productos.jar
CMD ["java", "-jar", "/home/Productos.jar"]