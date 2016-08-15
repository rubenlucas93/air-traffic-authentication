#!/bin/bash

rm ./output
touch ./output

./Servidor.sh | tee output
