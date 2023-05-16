FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y openjdk-17-jre
RUN apt-get install -y openjdk-17-jdk
COPY Usuarios.jar /home/Usuarios.jar
CMD ["java", "-jar", "/home/Usuarios.jar"]