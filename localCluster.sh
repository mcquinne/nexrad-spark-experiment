#!/usr/bin/env bash

# common flags
CLUSTER_PREFIX="nexrad"
DRIVER_OPTIONS="--driver virtualbox"

function create {
    # create master node
    MASTER_OPTIONS="$DRIVER_OPTIONS --engine-label role=master"
    MASTER_NAME="${CLUSTER_PREFIX}0"
    docker-machine create ${MASTER_OPTIONS} ${MASTER_NAME}
    MASTER_ADDR="$(docker-machine ip ${MASTER_NAME}):2377"
    docker-machine ssh ${MASTER_NAME} docker swarm init --advertise-addr ${MASTER_ADDR}
    WORKER_TOKEN="$(docker-machine ssh ${MASTER_NAME} docker swarm join-token worker -q)"

    # create worker nodes
    NUM_WORKERS="3"
    for n in $(seq 1 ${NUM_WORKERS}); do
      WORKER_NAME="${CLUSTER_PREFIX}${n}"
      docker-machine create ${DRIVER_OPTIONS} ${WORKER_NAME}
      docker-machine ssh ${WORKER_NAME} docker swarm join --token ${WORKER_TOKEN} ${MASTER_ADDR}
    done
}

function destroy {
    docker-machine ls | grep "^${CLUSTER_PREFIX}" | cut -d\  -f1 | xargs docker-machine rm -y
}

case $1 in
    "create")
        create
        ;;
    "destroy")
        destroy
        ;;
    *)
        echo "Usage: $0 create|destroy"
        exit 1
        ;;
esac
