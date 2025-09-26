# 📅 M-Pesa Scheduler – FinTech Automation App  

The **M-Pesa Scheduler App** is a modern fintech solution designed to simplify and automate mobile money transactions using the **Safaricom M-Pesa Daraja API**.It empowers individuals, SMEs, and organizations to schedule and execute payments such as rent, school fees, salaries, utility bills, and subscription services with minimal effort.By integrating **schedulers** (Quartz & Spring Scheduling), the app ensures that payments are triggered reliably at the right time, without manual intervention.It eliminates delays, reduces operational costs, and improves financial efficiency while providing a secure, auditable, and developer-friendly API.This project demonstrates a real-world **Kotlin + Spring Boot** architecture, robust scheduling, secure API integration, and scalable design, making it a valuable blueprint for fintech applications in Africa and beyond.  


## 🚀 Features  
- 🔄 Automate **recurring and one-time M-Pesa payments**  
- 🔒 Secure **authentication and authorization** with JWT  
- 📊 Real-time transaction logging, error handling & retries  
- 📅 Powered by **Quartz Scheduler** & **Spring Task Scheduling**  
- 📩 Optional notifications via email/SMS for payment updates  
- 🗄️ Transaction history & searchable audit logs  
- 🌍 Scalable design for individuals, SMEs, and enterprises  


## 🛠️ Tech Stack  

**Backend**  
- Kotlin + Spring Boot  
- PostgreSQL (transaction data, user profiles, logs)  
- Spring Data JPA / Hibernate  
- M-Pesa Daraja APIs (STK Push, C2B, B2C)  
- Quartz Scheduler / Spring Task Scheduling  
- RESTful APIs for secure integration  

**Frontend (optional)**  
- React.js or Angular for dashboard UI  
- Tailwind CSS for responsive design  

**DevOps & Others**  
- Docker / Docker Compose for containerization  
- Git & GitHub for version control  
- Postman/Insomnia for API testing  
- JWT for authentication & authorization  
- Monitoring/logging with Spring Actuator + ELK or Prometheus/Grafana  

## ⚡ Getting Started  

### Prerequisites  
- JDK 17+  
- Gradle or Maven  
- PostgreSQL running locally or via Docker  
- Safaricom M-Pesa Daraja API credentials 
