# Hotel Reservation System 🏨

A Java-based Hotel Reservation System built with **JDK 26**, **Apache Maven**, and **SQLite**.

---

## Prerequisites

Before running the application, make sure you have the following installed and configured on your machine:

1. **Java Development Kit (JDK) 26**
   * Download and install JDK 26.
   * Verify installation in terminal:
     ```cmd
     java -version
     ```
   * Ensure `JAVA_HOME` environment variable is set to your JDK path.

2. **Apache Maven**
   * Download and extract Apache Maven (e.g., `apache-maven-3.9.x`).
   * Add Maven's `bin` directory to your system's **PATH** environment variable (or set `MAVEN_HOME`).
   * Verify installation in terminal:
     ```cmd
     mvn -version
     ```

---

## Project Setup & First Run

1. **Clone the Repository**
   ```bash
   git clone [https://github.com/riellagrimassoscc-ui/HotelReservationSystem.git](https://github.com/riellagrimassoscc-ui/HotelReservationSystem.git)
   cd HotelReservationSystem


2. **Clean & Compile the Project**
    Run the following command to download dependencies (such as the SQLite JDBC driver), create/update the database schema from `schema.sql`, and compile the project:

    ```bash
    mvn clean compile exec:java
    ```
---

## Running the Application

### Option 1: Via Maven (Development Mode)

To quickly run the application during development:

```bash
mvn exec:java
```

### Option 2: Build & Run as Executable JAR (Production Mode)

1. **Package the application into a JAR file:**
```bash
mvn clean package
```


*This creates the compiled JAR file in the `target/` directory.*
2. **Run the generated JAR file:**
```bash
java -cp "target/HotelReservationSystem-1.0-SNAPSHOT.jar;target/dependency/*" com.sms.hotelreservationsystem.HotelReservationSystem

```



---

## Features & Tech Stack

* **Language:** Java (JDK 26)
* **Build Tool:** Apache Maven
* **Database:** SQLite JDBC Driver (`3.45.1.0`)
* **Features:** Modular DB Connection setup, automated schema migration via `schema.sql`, and parameterized queries.
* **Tech Stack:** Java, Maven, SQLite, JDBC.