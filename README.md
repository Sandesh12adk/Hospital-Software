# 🏥 Hospital Management System

A comprehensive Hospital Management System built with **Spring Boot** that handles patient records, doctor appointments, medical records, and department management with robust security features.

## ✨ Features

- **🔐 Role-based Access Control** (Admin, Doctor, Patient)
- **🛡️ JWT Authentication** for secure API access
- **📅 Appointment Management** with status tracking (Scheduled, Completed, Canceled)
- **🏥 Medical Records** with diagnosis and prescriptions
- **📊 Department Management** with doctor assignments
- **👨‍⚕️ Patient & Doctor Registration**
- **🔑 Password & Email Update** functionality
- **📖 Swagger API Documentation**

## 🛠️ Technology Stack

| Category          | Technologies                          |
|-------------------|---------------------------------------|
| **Backend**       | Java 17, Spring Boot 3.1.5            |
| **Security**      | Spring Security 6, JWT                |
| **Database**      | MySQL 8.0 (Hibernate ORM)             |
| **API Docs**      | Swagger 3                             |
| **Validation**    | Jakarta Validation                    |
| **Build Tool**    | Maven                                 |

## 🌐 API Endpoints

### 🔑 Authentication
- `POST /login` - Get JWT token
- `GET /accountinfo` - Get authenticated user info

### 👨‍💼 Admin Operations
- `POST /admin/register` - Register new admin
- `GET /user/patient/findall` - Get all patients
- `GET /appointment/findall` - Get all appointments
- `POST /department/save` - Create new department

### 👨‍⚕️ Doctor Operations
- `POST /user/doctor/register` - Doctor registration
- `PUT /user/doctor/make_as_schelduded/{appointmentId}` - Mark appointment as scheduled
- `PUT /user/doctor/make_as_canceled/{appointmentId}` - Cancel appointment
- `POST /medicalrecord/save` - Create medical record

### 👨‍⚕️ Patient Operations
- `POST /user/patient/register` - Patient registration
- `GET /user/patient/{patientId}` - Get patient details
- `GET /medicalrecord/find_by_patientid/{patientId}` - Get patient medical records

### 📅 Appointment Management
- `POST /appointment/create` - Create new appointment
- `GET /appointment/findbydoctorid/{docId}` - Get appointments by doctor
- `GET /appointment/find` - Get appointments by doctor and patient

### 👤 User Management
- `POST /update/password` - Change password
- `POST /update/email` - Update email

## 🔒 Security Implementation
- Role-based authorization with Spring Security
- JWT token authentication
- Password encryption with BCrypt
- CSRF protection
- Stateless session management

## 🗃️ Database Schema
The system uses a relational database with entities for:
- Users (with roles)
- Doctors
- Patients
- Departments
- Appointments
- Medical Records

## 🚀 Installation
1. Clone the repository
2. Configure database connection in `application.properties`
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

## 📚 API Documentation
Access Swagger UI at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) after starting the application

---
> <span style="color:red; font-weight:bold">
> ⚠️ Note: The admin account is auto created when the application first started if there is no admin account available (userName= admin, password= admin123). With this admin account, you can create your own admin account.  
> Once you create the account, the default admin account is auto deleted next time you start the application.
</span>

⭐ **Star this repo if you find it useful!** ⭐   
