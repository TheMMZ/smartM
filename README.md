# SmartM — Intelligent Maintenance Management Platform

> A full-stack, microservice-based **predictive maintenance** application for industrial equipment. SmartM combines real-time IoT sensor simulation, AI-powered failure prediction, and a role-based management interface accessible on both **web** and **Android**.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [First-Time Installation](#first-time-installation)
- [Environment Variables](#environment-variables)
- [Running the Application](#running-the-application)
- [Building the Android APK](#building-the-android-apk)
- [User Roles & Features](#user-roles--features)
- [API Gateway Routing](#api-gateway-routing)
- [IoT Simulation Pipeline](#iot-simulation-pipeline)

---

## Architecture Overview

```
┌─────────────┐     ┌──────────────────┐     ┌──────────────────────┐
│  Angular 22  │────▶│  Gateway Service  │────▶│  Identity Service     │
│  (Capacitor) │     │  (Spring Cloud)   │     │  (Auth / JWT / Users) │
└─────────────┘     │     :8888         │     │       :8081           │
                    │                    │────▶│  Equipment Service    │
┌─────────────┐     │                    │     │  (CRUD / Alerts)      │
│  Node-RED    │────▶│                    │     │       :8082           │
│  (IoT Sim)   │     └────────┬─────────┘     └──────────────────────┘
│   :1880      │              │
└─────────────┘     ┌────────▼─────────┐     ┌──────────────────────┐
                    │ Discovery Service │     │  Prediction Service   │
                    │  (Eureka) :8761   │     │  (FastAPI + ONNX)     │
                    └──────────────────┘     │       :8000           │
                                             └──────────────────────┘
                                                       │
                                             ┌─────────▼──────────┐
                                             │  PostgreSQL 16      │
                                             │       :5432         │
                                             └────────────────────┘
```

---

## Tech Stack

| Layer              | Technology                                                      |
| :----------------- | :-------------------------------------------------------------- |
| **Frontend**       | Angular 22, TypeScript 6, SCSS, Capacitor 8 (Android)           |
| **API Gateway**    | Spring Cloud Gateway (Spring Boot 3.2)                          |
| **Service Discovery** | Netflix Eureka (Spring Cloud 2023.0)                         |
| **Identity**       | Spring Boot 3.2, Spring Security, JWT, JPA/Hibernate            |
| **Equipment**      | Spring Boot 3.2, JPA/Hibernate, REST API                        |
| **Prediction AI**  | Python 3, FastAPI, ONNX Runtime, NumPy                          |
| **IoT Simulation** | Node-RED (sensor data generation & ML pipeline orchestration)   |
| **Database**       | PostgreSQL 16                                                   |
| **Build Tools**    | Maven (Java), npm (Angular), Gradle (Android)                   |
| **Mobile**         | Capacitor 8, Android SDK, Gradle                                |

---

## Project Structure

```
SmartM/
├── .env                          # Environment variables (DB, JWT, API keys)
├── docker-compose.yml            # PostgreSQL container
├── run_all.ps1                   # PowerShell script to start all services
├── flows.json                    # Node-RED IoT simulation flow
│
├── backend/
│   ├── pom.xml                   # Parent Maven POM (Spring Boot 3.2 / Cloud 2023.0)
│   ├── mvnw / mvnw.cmd          # Maven wrapper
│   ├── discovery-service/        # Eureka Server (:8761)
│   ├── gateway-service/          # Spring Cloud Gateway (:8888)
│   ├── identity-service/         # Auth, Users, Teams, Tasks, Notifications (:8081)
│   ├── equipement-service/       # Equipment CRUD, Taxonomy, Alerts (:8082)
│   └── prediction-service/       # Python FastAPI + ONNX model (:8000)
│       └── ai study/work/api/
│           ├── main.py
│           ├── requirements.txt
│           └── multitask_model.onnx
│
├── frontend/
│   ├── package.json              # Angular 22 project
│   ├── capacitor.config.ts       # Capacitor (Android) config
│   ├── angular.json
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.routes.ts     # Role-guarded routing
│   │   │   ├── guards/           # Role-based route guards
│   │   │   ├── services/         # AuthService, DataService
│   │   │   ├── utils/            # API base URL config
│   │   │   ├── components/       # Chatbot widget
│   │   │   └── pages/
│   │   │       ├── home/         # Landing page
│   │   │       ├── login/        # Auth page
│   │   │       ├── dashboard/    # App shell (sidebar/bottom nav)
│   │   │       ├── dashboard-overview/
│   │   │       ├── admin-manager/# Admin/Manager control panel
│   │   │       ├── technician/   # Task execution view
│   │   │       ├── engineer/     # Engineering station
│   │   │       ├── predictions/  # AI predictions dashboard
│   │   │       ├── team/         # Team management
│   │   │       ├── stock/        # Inventory management
│   │   │       ├── rapport/      # Maintenance reports
│   │   │       ├── approvals/    # Manager approval workflow
│   │   │       └── chat/         # AI assistant (Gemini)
│   │   └── styles.scss           # Global styles
│   └── android/                  # Capacitor Android project
│       ├── gradlew.bat
│       └── app/build/outputs/apk/debug/app-debug.apk
```

---

## Prerequisites

Before starting, make sure you have the following installed:

| Tool          | Version  | Purpose                          |
| :------------ | :------- | :------------------------------- |
| **Java JDK**  | 21+      | Backend microservices            |
| **Node.js**   | 20+      | Frontend & Node-RED              |
| **npm**       | 10+      | Package management               |
| **Python**    | 3.10+    | Prediction service               |
| **Docker**    | Latest   | PostgreSQL container             |
| **Android SDK** | API 34+ | Mobile APK build (optional)      |
| **ADB**       | Latest   | APK installation (optional)      |
| **Node-RED**  | Latest   | IoT simulation (`npm i -g node-red`) |

---

## First-Time Installation

### 1. Clone the Repository

```bash
git clone --recurse-submodules https://github.com/TheMMZ/smartM-V2.git
cd smartM-V2
```

> **Important:** This repo uses **Git submodules** for each service. The `--recurse-submodules` flag pulls all of them.

If you've already cloned without submodules:

```bash
git submodule update --init --recursive
```

### 2. Start the Database

```bash
docker compose up -d
```

This starts a PostgreSQL 16 container on port `5432` with:
- **Database:** `smartmaintain_db`
- **User:** see `.env`
- **Password:** see `.env`

### 3. Configure Environment Variables

Create a `.env` file at the project root (if not present):

```env
DB_USERNAME=karima
DB_PASSWORD=karima123
JWT_SECRET=YourSuperLongSecretKeyAtLeast32Characters
GEMINI_API_KEY=your_gemini_api_key_here
```

### 4. Install Backend Dependencies

The Maven wrapper is included — no global Maven installation needed.

```bash
cd backend
./mvnw clean install -DskipTests
```

> On Windows, use `mvnw.cmd` instead of `./mvnw`.

### 5. Install Frontend Dependencies

```bash
cd frontend
npm install
```

### 6. Install Prediction Service Dependencies

```bash
cd backend/prediction-service/ai\ study/work/api
pip install -r requirements.txt
```

### 7. Install Node-RED (if not already installed)

```bash
npm install -g node-red
```

---

## Environment Variables

| Variable         | Description                        | Default              |
| :--------------- | :--------------------------------- | :------------------- |
| `DB_USERNAME`    | PostgreSQL username                | `karima`             |
| `DB_PASSWORD`    | PostgreSQL password                | `karima123`          |
| `JWT_SECRET`     | Secret key for JWT token signing   | *(required)*         |
| `GEMINI_API_KEY` | Google Gemini API key for AI chat  | *(optional)*         |
| `ANDROID_HOME`   | Path to Android SDK                | `C:\Android`         |

---

## Running the Application

### Option A: Start Everything at Once (Windows)

```powershell
.\run_all.ps1
```

This script starts all services in separate terminal windows:
1. Discovery Service (Eureka) — waits 15s for initialization
2. Gateway Service
3. Identity Service
4. Equipment Service
5. Frontend (`ng serve`)
6. Prediction Service (`uvicorn`)
7. Node-RED

### Option B: Start Services Manually

Start each service **in this order** (discovery must be first):

```bash
# Terminal 1 — Discovery Service (Eureka)
cd backend/discovery-service
../mvnw spring-boot:run -DskipTests

# Terminal 2 — Gateway Service (wait ~15s for Eureka)
cd backend/gateway-service
../mvnw spring-boot:run -DskipTests

# Terminal 3 — Identity Service
cd backend/identity-service
../mvnw spring-boot:run -DskipTests

# Terminal 4 — Equipment Service
cd backend/equipement-service
../mvnw spring-boot:run -DskipTests

# Terminal 5 — Prediction Service
cd backend/prediction-service/ai\ study/work/api
uvicorn main:app --port 8000

# Terminal 6 — Frontend
cd frontend
npm start

# Terminal 7 — Node-RED (IoT Simulation)
node-red
```

### Access Points

| Service            | URL                          |
| :----------------- | :--------------------------- |
| **Frontend (Web)** | http://localhost:4200         |
| **Eureka Dashboard** | http://localhost:8761       |
| **API Gateway**    | http://localhost:8888         |
| **Prediction API** | http://localhost:8000/docs    |
| **Node-RED Editor** | http://localhost:1880        |

---

## Building the Android APK

### 1. Update the API Base URL

Edit `frontend/src/app/utils/api.config.ts` and set the IP to your PC's LAN address:

```typescript
export const getApiBaseUrl = () => {
  return 'http://<YOUR_PC_LAN_IP>:8888';
};
```

> Find your LAN IP with `ipconfig` (Windows) or `ifconfig` (Mac/Linux).

### 2. Build & Sync

```bash
cd frontend
npm run build
npx cap sync android
```

### 3. Compile the APK

```bash
cd android
.\gradlew.bat assembleDebug
```

### 4. Install on Device

```bash
adb install android/app/build/outputs/apk/debug/app-debug.apk
```

> **Note:** Enable **"Install via USB"** in your phone's Developer Options to avoid `INSTALL_FAILED_USER_RESTRICTED` errors.

---

## User Roles & Features

| Role          | Dashboard | Admin Panel | Tasks | Predictions | Teams | Stock | Reports | Approvals | AI Chat |
| :------------ | :-------: | :---------: | :---: | :---------: | :---: | :---: | :-----: | :-------: | :-----: |
| **Admin**     | —         | ✓           | —     | ✓           | —     | ✓     | ✓       | —         | ✓       |
| **Manager**   | ✓         | ✓           | ✓     | ✓           | —     | —     | ✓       | ✓         | ✓       |
| **Engineer**  | ✓         | —           | ✓     | ✓           | ✓     | —     | ✓       | —         | ✓       |
| **Technician**| —         | —           | ✓     | ✓           | ✓     | —     | ✓       | —         | ✓       |

### Role Capabilities

- **Admin** — Full system control: user account management (create, approve, suspend, delete), team CRUD, engine/taxonomy management, stock, and global task oversight.
- **Manager** — Operational oversight: task creation & assignment, team performance tracking, report approvals, and KPI dashboards.
- **Engineer** — Team leadership: task creation, execution tracking, team coordination, and maintenance report writing.
- **Technician** — Field execution: task completion, sub-task management, technician notes, and report submission.

---

## API Gateway Routing

All API requests go through the Gateway at `:8888`:

| Route Prefix                          | Target Service     |
| :------------------------------------ | :----------------- |
| `/api/identity-service/**`            | Identity Service   |
| `/api/equipement-service/**`          | Equipment Service  |

---

## IoT Simulation Pipeline

The **Node-RED** flow (`flows.json`) orchestrates a real-time IoT simulation:

```
[Trigger 10s] → [GET /equipements] → [Split Engines] → [Simulate 14 ML Features]
    → [Buffer 30 Cycles] → [POST /predict (FastAPI)] → [Format Alert] → [POST /alerts/log]
```

1. Every **10 seconds**, Node-RED fetches all registered engines from the Equipment Service.
2. For each engine, it **simulates 14 sensor features** (vibration, temperature, etc.) with variance based on engine status.
3. It **buffers 30 cycles** per engine to build a time-series window.
4. The window is sent to the **FastAPI Prediction Service**, which runs inference through an **ONNX model** to predict Remaining Useful Life (RUL) and alert status.
5. Results are logged back to the Equipment Service as **alerts**.

---

## License

This project is developed for academic purposes.
