#!/bin/bash

rm ./output
touch ./output

./Receptor | tee output
