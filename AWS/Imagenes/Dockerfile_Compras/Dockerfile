FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y openjdk-17-jre
RUN apt-get install -y openjdk-17-jdk
COPY Compras.jar /home/Compras.jar
CMD ["java", "-jar", "/home/Compras.jar"]