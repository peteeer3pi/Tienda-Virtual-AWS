FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y openjdk-17-jre
RUN apt-get install -y openjdk-17-jdk
COPY Carritos.jar /home/Carritos.jar
CMD ["java", "-jar", "/home/Carritos.jar"]