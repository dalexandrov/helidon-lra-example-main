#!/bin/bash
VERSION=1.0
NS=f1
DIR=$(pwd)

for service in 'f1-booking-service' 'f1-seat-booking-service' 'f1-payment-service';
  do
    cd "${DIR}/${service}" || exit
    tag="${NS}/${service}:${VERSION}"
    executed_cmd="docker build -t ${tag} ."
    echo "${executed_cmd}" && eval "${executed_cmd}"
  done