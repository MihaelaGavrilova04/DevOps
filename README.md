# BlushBox DevOps Project 🌸

## Disclaimer

This is a Spring Boot learning project designed to demonstrate DevOps and
CI/CD pipeline concepts. It is for educational purposes only and does not
handle sensitive information or implement production-grade security.

---

## Project Overview

BlushBox is a simple, pink-themed educational project built with Spring Boot
and Thymeleaf. It demonstrates a basic microservice architecture where users
can manage documents in a simulated environment.

---

## DevOps Practices Utilized

### Source Control and Collaboration

All code is managed in a GitHub repository. Changes are proposed, reviewed,
and integrated systematically.

* main branch: holds the stable, production-ready code
* app-fixes branch: used for active development and new features
* k8s branch: used for Kubernetes configuration

---

### Automated Pipelines

Two automated workflows are implemented using GitHub Actions.

* pr-validation.yml: quality gate for all pull requests
* main-cicd.yml: delivery pipeline triggered after merge to main

---

## Pipeline Details

### Continuous Integration (PR Quality Gate)

The pr-validation.yml pipeline enforces quality from the start.

* Runs all 60 tests

  * 31 frontend tests
  * 29 documents service tests
* Enforces code style using Checkstyle
* Checks code coverage with JaCoCo (minimum 30 percent)
* Validates database migrations with Flyway
* Blocks the merge if any check fails

---

### Continuous Delivery (Main Deployment Pipeline)

Once code is merged into main, the main-cicd.yml pipeline is executed.

* Code quality analysis using SonarQube
* Build and package services into Docker containers
* Security scanning of images with Trivy
* Push images to GitHub Container Registry
* Deploy a test environment using a local k3d cluster

---

## Infrastructure and Deployment

### Docker

Both microservices are containerized to ensure portability and consistency
across all environments.

---

### Kubernetes

#### Local Development Environment

For daily development and testing, the application is deployed to a local
Kubernetes cluster provided by Docker Desktop. This single-node cluster is
managed directly by Docker Desktop and serves as the primary development
environment.

#### CI/CD Pipeline Environment

As part of the main-cicd.yml pipeline, a clean and temporary k3d cluster is
automatically created. This environment is used for integration testing and
ensures that each merge to main is validated in an isolated, production-like
setup.

#### Consistent Configuration

Both local and CI environments use the same declarative Kubernetes manifests
stored in the k8s directory. This Infrastructure as Code approach guarantees
consistent behavior across environments.

---

## Infrastructure as Code

All infrastructure components are defined in version-controlled YAML files
located in the k8s directory.

* Database configuration
* Storage configuration using MinIO
* Application deployments
* Secrets and configuration objects

---

## S3-Compatible Storage

MinIO is used for document storage and provides full Amazon S3 API
compatibility. This allows local development while applying cloud-native
storage patterns.

---

## Security and Maintenance

### Security Scanning

* Static code analysis with SonarQube
* Container vulnerability scanning with Trivy

### Secrets Management

Sensitive data is stored securely in GitHub Secrets and is never hard-coded
in the repository.

### Database Management

Flyway manages database schema changes using version-controlled SQL
migrations, ensuring reliable and repeatable evolution.

---

## Summary

BlushBox implements a complete CI/CD pipeline that demonstrates core DevOps
principles. It automates quality checks, integrates security scanning,
packages applications with Docker, manages infrastructure as code, and
deploys consistently using Kubernetes.

While built for learning purposes, the project uses real, industry-standard
tools to provide practical, hands-on DevOps experience.

Enjoy exploring BlushBox 🌸

