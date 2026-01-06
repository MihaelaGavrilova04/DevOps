# BlushBox DevOps Project 🌸

**Disclaimer:** This is a Spring Boot learning project designed to demonstrate DevOps and CI/CD pipeline concepts.
It is for educational purposes only and does not handle sensitive information or implement production-grade security.

BlushBox is a simple, pink-themed educational project built with Spring Boot and Thymeleaf. 
It demonstrates a basic microservice architecture where users can manage documents in a simulated environment.

---

## DevOps practices utilized

### Source Control & Collaboration

All code is managed in a GitHub repository. Changes are proposed, reviewed, and integrated systematically.

*   **main branch:** Holds the stable, production-ready code.
*   **app-fixes branch:** Used for active development and new features.
*   **k8s branch:** Used for adding Kubernetes configuration.

### Automated Pipelines

Two automated workflows in GitHub Actions:

*   **`pr-validation.yml`:** The quality gate that runs automatically on every pull request.
*   **`main-cicd.yml`:** The delivery pipeline that runs after code is successfully merged into `main`.

---

## Pipeline Details

### Continuous Integration (PR Quality Gate)

The `pr-validation.yml` pipeline enforces quality from the start:

*   Runs all 60 tests (31 for frontend, 29 for documents service).
*   Enforces code style using Checkstyle.
*   Checks code coverage with JaCoCo (minimum 30%).
*   Validates database migrations with Flyway.
*   Blocks the merge if any check fails.

### Continuous Delivery (Main Deployment Pipeline)

Once code merges to `main`, the `main-cicd.yml` pipeline takes over:

*   **Code Quality Analysis** – SonarQube performs deep static analysis.
*   **Build & Package** – Services are built into Docker containers.
*   **Security Scan** – Docker images scanned with Trivy.
*   **Push to Registry** – Images pushed to GitHub Container Registry.
*   **Test Environment Deployment** – Infrastructure stack provisioned in a local k3d cluster for integration testing.

---

## Infrastructure & Deployment

### Docker

Both microservices are containerized for portability and consistency across environments.

### Kubernetes

**Local Development with Docker Desktop Kubernetes**

For daily development and testing, the application is deployed to a local Kubernetes cluster provided by Docker Desktop. 
This single-node cluster is provisioned and managed directly within Docker Desktop, allowing for a fast and efficient local testing loop. 
This environment is the primary "development" cluster.

**Ephemeral CI/CD Pipeline Environment**

As part of the `main-cicd.yml` pipeline in GitHub Actions, a clean, temporary k3d cluster is automatically created for integration testing and validation.
This ensures that every change merged into the main branch is proven to deploy correctly in an isolated, production-like environment.

**Consistent Configuration**

Both environments (local Docker Desktop and CI/CD k3d) deploy using the same declarative YAML configurations from the `k8s/` directory.
This principle of "Infrastructure as Code" guarantees that the application behaves identically from a developer's laptop to the automated pipeline.

### Infrastructure as Code

Every component is defined in version-controlled YAML files within the `k8s/` directory:

*   Database configurations
*   Storage setup (MinIO)
*   Application deployments
*   Secrets and configurations

### S3-Compatible Storage with MinIO

For document storage, we use MinIO with full Amazon S3 API compatibility.
This means we develop locally but learn the same patterns used with major cloud storage services.

---

## Security & Maintenance

### Security Scanning

*   Code analysis with SonarQube
*   Container vulnerability scanning with Trivy

### Secrets Management

Sensitive data is stored securely in GitHub Secrets, never hard-coded in the repository.

### Database Management

Flyway manages schema changes through version-controlled SQL files, ensuring reliable database evolution.

---

## Summary

This project implements a complete, working CI/CD pipeline that demonstrates core DevOps principles. 
It shows how to automate quality checks, integrate security, package applications with Docker, 
manage infrastructure as code, and deploy consistently using Kubernetes. While built for learning, the pipeline uses real,
industry-standard tools to provide practical, hands-on experience.

*Enjoy exploring BlushBox!* 🌸
