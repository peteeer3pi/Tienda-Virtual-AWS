#!/bin/bash

docker stack deploy -c docker-stack.yml enjambreAPI
sleep 5
docker exec $(docker ps -q -f name=bd) mongoimport --type json -d tiendaonline --jsonArray -c productos /tmp/productos.json