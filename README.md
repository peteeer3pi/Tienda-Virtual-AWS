# Tienda-Virtual-AWS

Tienda virtual sencilla que consta de un carrito de la compra, que permite seleccionar productos, añadirlos al carrito de la compra y realizar una compra.


# Para desplegar la aplicación en AWS seguir los siguientes pasos:

1- Crear un grupo de seguridad que permita todo el tráfico (para simplificar el despliegue) tanto en entradas como en salidas
2- Crear 5 instancias Amazon Linux 2 AMI y procesador t2.micro (los valores que vienen por defecto). Llamarlas M1, W1, W2, W3 y W4
3- Dentro de esas instancias ejecutar los siguientes comandos para configurarlas:

sudo yum update -y;sudo yum install -y docker;sudo usermod -aG docker ec2-user;sudo service docker status;sudo service docker start;sudo systemctl enable docker;mkdir Imagenes;cd Imagenes;mkdir Dockerfile_Compras;mkdir Dockerfile_Carritos;mkdir Dockerfile_Productos;mkdir Dockerfile_Mongo;mkdir Dockerfile_Usuarios

4- Reiniciar las instancias para aplicar los cambios correctamente
5- En la máquina M1 ejecutar el comando siguiente:

docker swarm init

6- En las otras cuatro máquinas ejecutar el comando que ha proporcionado el comando anterior
7- Transferir los archivos de la carpeta Imagenes a sus correspondientes carpetas en todas las instancias
8- Transferir los archivos desplegar_aplicacion.sh y docker-stack.yml a la máquina M1
9- En cada una de las máquinas, establecer el directorio de trabajo en la carpeta Imagenes y ejecutar los siguientes comandos:

cd Dockerfile_Carritos;docker build -t carritos .;cd ../Dockerfile_Productos;docker build -t productos .;cd ../Dockerfile_Compras;docker build -t compras .;cd ../Dockerfile_Mongo;docker build -t imagen_bd .;cd ../Dockerfile_Usuarios;docker build -t usuarios .

10- Ejecutar el siguiente comando para darle el formato adecuado al Script:
sed -e "s/\r//g" desplegar_aplicacion.sh > desplegar_aplicacion_correcto.sh

11- Proporcionarle permisos de ejecución al Script desplegar_aplicacion_correcto.sh:

chmod +x desplegar_aplicacion_correcto.sh

12- Ejecutar el Script desplegar_aplicacion_correcto.sh para iniciar el enjambre

Este proceso creará un enjambre de nombre "enjambreAPI", donde la máquina M1 tendrá siempre la base de datos y los demás servicios
se distribuirán entre las otras cuatro máquinas.
Para acceder a las páginas OpenAPI de cada servicio:

Productos -> ipPúblicaCualquierInstancia:5010/swagger-ui.html
Carritos -> ipPúblicaCualquierInstancia:5011/swagger-ui.html
Compras -> ipPúblicaCualquierInstancia:5012/swagger-ui.html
Usuarios -> ipPúblicaCualquierInstancia:5013/swagger-ui.html