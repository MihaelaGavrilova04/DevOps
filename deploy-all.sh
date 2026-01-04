#!/bin/bash

kubectl cluster-info
if [ $? -ne 0 ]; then
    echo "k8s not reachable"
    exit 1
fi

kubectl apply -f k8s/namespace.yaml

kubectl apply -f k8s/secrets/

kubectl apply -f k8s/storage/

kubectl apply -f k8s/postgres/

kubectl wait --for=condition=ready pod -l app=postgres-document --timeout=180s -n file-app

if [ $? -eq 0 ]; then
    echo "PostgreSQL Document DB ready"
else
    echo "PostgreSQL Document DB failed for the timing"
fi

kubectl wait --for=condition=ready pod -l app=postgres-user --timeout=180s -n file-app
if [ $? -eq 0 ]; then
    echo "PostgreSQL User DB ready"
else
    echo "PostgreSQL User DB failed for the timing"
fi


kubectl apply -f k8s/minio/

kubectl wait --for=condition=ready pod -l app=minio --timeout=120s -n file-app

if [ $? -eq 0 ]; then
    echo "MinIo ready!"
else
    echo "MinIO failed for the timing"
fi


# Build docker images:

cd documents-service
docker build -t documents-service:latest .
if [ $? -ne 0 ]; then
    echo "Error building docker image of documents-service!"
    exit 1
fi
cd ..

cd frontend-user
docker build -t frontend-user:latest .
if [ $? -ne 0 ]; then
    echo "Error building docker image of frontend-user!"
    exit 1
fi
cd ..


# Deployment:

kubectl apply -f k8s/services/

#Debug
kubectl get pods -n file-app

kubectl get svc -n file-app
