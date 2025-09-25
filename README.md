# 🏫 E-Learning Platform – Full Project Description

A complete **E-Learning Platform** supporting **3 types of users**:

👩‍🎓 **Student** – 👨‍🏫 **Teacher** – 🛠️ **Admin**  

✅ Full **Authentication & Authorization** implemented with **Spring Security + JWT** to ensure secure role-based access to all resources.

---

## ✨ Key Features

### 🛠️ Admin
* Approves or rejects teacher accounts with email and notification updates.
* Monitors student payments and manages all transactions.
* Activates or rejects courses after review, sending notifications to the respective teacher.
* Live Dashboard to track the entire platform .

### 👨‍🏫 Teacher
* Creates new courses and adds **sessions** (video lessons) with uploaded media.
* Adds exams with customizable questions and correct answers, and can update question options.
* Approves or rejects student requests (e.g., extra views, exam retakes) with instant notifications.
* Tracks student grades and exam results.
* Sends a request to add a new specialization if not available.
* Manages all details of their own courses securely.

### 👩‍🎓 Student
* Watches course videos with a limited number of views set by the teacher.
* Sends requests for extra views or exam retakes.
* Takes exams and sees results instantly.
* Accesses all available courses and past exams with full details.
* Can view course content (sessions, exams) **only** if enrolled in that course.

---

## 🛠️ Backend Tech Stack

- **Spring Boot** as the application framework.
- **Spring Security + JWT** for authentication and role-based authorization.
- **Spring Data JPA + Hibernate** for database operations.
- **MySQL** as the relational database.
- **Cloudinary** for media uploads (images, videos).
- **JavaMailSender** for email notifications (account/course approval or rejection).
- **WebSocket & SSE** for live notifications and dashboard data.
- **Lombok** to reduce boilerplate code (`@Data`, `@Builder`, etc.).
- **Validation API** for request data validation.
- **MapStruct / DTO Mapping** to separate entities from API responses.
- **Global Exception Handling** for consistent and clean error responses.
- **Swagger & Postman** for testing Apis.

---

## 📂 Major API Endpoints

| Role        | Key Endpoints |
|-------------|---------------|
| **Admin**   | Approve/Reject teacher accounts, Activate/Reject courses, Manage payments, Send notifications ,Live Dashboard. |
| **Teacher** | Create/Update/Delete courses, Add sessions (video lessons), Upload exams and questions, Update answers, Approve/Reject student requests. |
| **Student** | Enroll in courses, View enrolled course details, Access sessions of enrolled courses, Take exams and view results, Submit requests for retake or extra views. |
| **Auth**    | User registration (student/teacher), Login, Password reset. |

---

## 💡 Additional Features

* **Notifications System**  
  - Students receive notifications when their requests are approved or rejected.  
  - Teachers receive notifications when their accounts or courses are approved or rejected.  
  - Automatic email sending using scheduled tasks.

* **Profile Management**  
  - Each user has a profile page to view and update their information.

* **Payments**  
  - Integrated payment system to handle course enrollments under admin supervision.

---

## 🚀 Integration
- RESTful API ready for integration with any **Front-End** (Web or Mobile).
- Scalable for future enhancements such as live streaming or real-time chat using **WebSocket or SSE**.

---

## 📊 Value
A secure, scalable educational platform managing the entire learning process:  
**Course Creation → Content Management → Exams  → Notifications → Payments**.

# UML 
```mermaid
classDiagram
    class User {
        +id : int
        +name : varchar
        +email : varchar
        +password : varchar
        +role : enum
        +created_at : datetime
    }

    class Course {
        +id : int
        +title : varchar
        +description : varchar
        +price : decimal
        +status : enum(ACTIVE, PENDING, REJECTED)
        +teacher_id : int
    }

    class Session {
        +id : int
        +title : varchar
        +video_url : varchar
        +course_id : int
    }

    class Enrollment {
        +id : int
        +enrollment_date : datetime
        +course_id : int
        +student_id : int
    }

    class Exam {
        +id : int
        +title : varchar
        +course_id : int
    }

    class Question {
        +id : int
        +text : varchar
        +type : varchar
        +exam_id : int
    }

    class Option {
        +id : int
        +text : varchar
        +correct : bit
        +question_id : int
    }

    class ExamSubmission {
        +id : int
        +score : int
        +submitted_at : datetime
        +exam_id : int
        +student_id : int
    }

    class ExamAnswer {
        +id : int
        +is_correct : bit
        +question_id : int
        +option_id : int
        +submission_id : int
    }

    class StudentRequest {
        +id : int
        +status : enum(APPROVED, EXPIRED, PENDING, REJECTED)
        +type : enum(RETAKE_EXAM)
        +course_id : int
        +exam_id : int
        +student_id : int
    }

    class Payment {
        +id : int
        +amount : decimal
        +created_at : datetime
        +status : enum(FAILED, PENDING, SUCCESS)
        +course_id : int
        +student_id : int
    }

    class Notification {
        +id : int
        +title : varchar
        +message : varchar
        +created_at : datetime
        +sent : bit
        +user_id : int
    }

    class Token {
        +id : int
        +token : varchar
        +token_type : enum(BEARER, REFRESH)
        +expired : bit
        +revoked : bit
        +user_id : int
    }

    %% Relationships
    User "1" --> "many" Course : teaches
    User "1" --> "many" Enrollment : enrolls
    User "1" --> "many" ExamSubmission : submits
    User "1" --> "many" StudentRequest : requests
    User "1" --> "many" Payment : makes
    User "1" --> "many" Notification : receives
    User "1" --> "many" Token : owns

    Course "1" --> "many" Session : contains
    Course "1" --> "many" Exam : has
    Course "1" --> "many" Enrollment : enrollments
    Course "1" --> "many" Payment : payments
    Course "1" --> "many" StudentRequest : requests

    Exam "1" --> "many" Question : includes
    Exam "1" --> "many" ExamSubmission : submissions
    Exam "1" --> "many" StudentRequest : retakeRequests

    Question "1" --> "many" Option : options
    Question "1" --> "many" ExamAnswer : answers

    ExamSubmission "1" --> "many" ExamAnswer : answers

```
 
 # ERD
```mermaid
erDiagram
    USER {
        int id PK
        varchar name
        varchar email
        varchar password
        enum role
    }

    COURSE {
        int id PK
        varchar title
        varchar description
        decimal price
        enum status
        int teacher_id FK
    }

    SESSION {
        int id PK
        varchar title
        varchar video_url
        int course_id FK
    }

    ENROLLMENTS {
        int id PK
        datetime enrollment_date
        int course_id FK
        int student_id FK
    }

    EXAM {
        int id PK
        varchar title
        int course_id FK
    }

    QUESTION {
        int id PK
        varchar text
        varchar type
        int exam_id FK
    }

    OPTION {
        int id PK
        varchar text
        bit correct
        int question_id FK
    }

    EXAM_SUBMISSION {
        int id PK
        int score
        datetime submitted_at
        int exam_id FK
        int student_id FK
    }

    EXAM_ANSWER {
        int id PK
        bit is_correct
        int question_id FK
        int option_id FK
        int submission_id FK
    }

    STUDENT_REQUEST {
        int id PK
        enum status
        enum type
        int course_id FK
        int exam_id FK
        int student_id FK
    }

    PAYMENTS {
        int id PK
        decimal amount
        datetime created_at
        enum status
        int course_id FK
        int student_id FK
    }

    NOTIFICATION {
        int id PK
        datetime created_at
        varchar title
        varchar message
        bit sent
        int user_id FK
    }

    TOKEN {
        int id PK
        varchar token
        enum token_type
        bit expired
        bit revoked
        int user_id FK
    }

    %% Relationships
    USER ||--o{ COURSE : "teaches"
    USER ||--o{ ENROLLMENTS : "enrolls"
    USER ||--o{ EXAM_SUBMISSION : "submits"
    USER ||--o{ STUDENT_REQUEST : "creates"
    USER ||--o{ PAYMENTS : "makes"
    USER ||--o{ NOTIFICATION : "receives"
    USER ||--o{ TOKEN : "owns"

    COURSE ||--o{ SESSION : "has"
    COURSE ||--o{ EXAM : "has"
    COURSE ||--o{ ENROLLMENTS : "has"
    COURSE ||--o{ PAYMENTS : "has"
    COURSE ||--o{ STUDENT_REQUEST : "requested"

    EXAM ||--o{ QUESTION : "contains"
    EXAM ||--o{ EXAM_SUBMISSION : "receives"
    EXAM ||--o{ STUDENT_REQUEST : "retake"

    QUESTION ||--o{ OPTION : "offers"
    QUESTION ||--o{ EXAM_ANSWER : "answered"

    EXAM_SUBMISSION ||--o{ EXAM_ANSWER : "includes"


```

