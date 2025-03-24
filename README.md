# PROJECT BACKEND

This is the backend service for backend, built using Spring Boot. It provides RESTful API endpoints and connects to a PostgreSQL database for data storage and retrieval.

## Requirements

To run this project, you'll need to have the following installed on your environment:

- **Java 17** or later
- **Gradle 7.0** or later
- **Docker** for hosting database (postgre container by default, but can change it in application.properties accordingly)
- **Postman** (optional, for testing APIs)

### Java

You can download and install Java from the official website: [Java SE Downloads](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).

Verify the installation:

```bash
$ java -version
```
### Gradle
Gradle is used to build and manage dependencies for this project. You can install Gradle via the instructions on the official website, or simply use the Gradle Wrapper included in the project:
            
    ./gradlew build
This command can be used after downloading the project, as shown in the "For Local Development Setup Instructions"
### Docker 
You can download Docker Desktop here [Docker Desktop](https://www.docker.com/) for a GUI applicaton or just [Engine](https://docs.docker.com/engine/install/) and run the commands in cmd
*Commands shown in the "For Local Development Setup Instructions"*

### Postman (Optional)
For endpoint testing you can download Postman here [Postman](https://postman.com/)

## For Local Development Setup Instructions

    git clone https://github.com/heohak/ORNet.git
    cd heohak/ORNet
    
- Or when Using IDEs just press "Get From VCS" and put the same URL: https://github.com/heohak/ORNet.git
- Use Gradle to build the project and download dependencies:

        ./gradlew clean build
    
- Or in the IDE you can also press gradle on the right side and then Tasks/build/build
- Ensure Docker is running, then start the PostgreSQL container:
  Run the next command in a command line (cmd), name, password, username and port can be be changed

        docker run --name postgres-container -e POSTGRES_USER=youruser -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=yourdb -p 5432:5432 -d postgres
  
Adjust the database configuration in src/main/resources/application.properties if you're using a different database or custom credentials.
- Run the Spring Boot application:

      ./gradlew bootRun
  Ensure that the PostgreSQL container is running before starting the application.

## Deployment
1. Docker Setup: You can deploy the backend and frontend applications along with the PostgreSQL database using Docker. The docker-compose.yml file provided defines the setup for the services, which includes the database, backend, and frontend.

2. Steps for Deployment
- Go to the correct directory

        cd heohak/ORNet
   
- Build and run services:
Make sure you have Docker installed and running, then execute the following command to build and start all services:

        docker-compose up --build
   
This will build the backend and frontend images, spin up the PostgreSQL container, and expose the services on the appropriate ports (5432 for PostgreSQL, 8080 for backend, 3000 for frontend).
- Environment Variables:
Update the .env file or directly modify the docker-compose.yml if you need to change the database credentials or other environment variables.

3. Access the application:

Frontend: Open a browser and go to http://localhost:3000 to view the frontend.
LDAP has to be also set up to be able to login.

Backend: The backend API will be available at http://localhost:8080/api. You can use Postman or similar tools to test the endpoints.
## Project Structure

- `logs/`                         # Application logs
- `uploads/`                      # Files that are uploaded
- `src/main/`                     # Source code
    - `resources/`                # Contains application properties
    - `java/com/demo/bait/`       # Source code 
        - `components/`           # Software components for various tables/entities.
        - `config/`               # Configuration files for CORS, security, etc.
        - `controller/`           # REST controllers for handling API requests.
        - `converter/`            # Converters for transforming data.
        - `dto/`                  # Data Transfer Objects for entities.
        - `entity/`               # Entity classes corresponding to database tables.
        - `enums/`                # Enum classes that define constants used for MaintenanceStatus and TrainingType
        - `mapper/`               # Mappers for converting entities to DTOs.
        - `repository/`           # Repositories for interacting with the database.
        - `Security/`             # Security-related configurations and classes, such as authentication and authorization logic.
        - `service/`              # Business logic and service layer.
        - `specification/`:       # Specifications for querying the database.
        - `BaitApplication.java`  # Main file that runs the program
- `Dockerfile`                    # A file that defines the steps to build a Docker image for the backend application.It sets the Java environment, copying the project files, and building the project with Gradle.
- `docker-compose.yml`            # Configures PostgreSQL database, backend that connects to it, and a frontend. It sets up database credentials, persists data, and ensures the backend starts after PostgreSQL, with                                        port mappings for each service."
- `REAMDE.md`                     # Documentation
## Database Diagram
![CRM_diagram](https://github.com/user-attachments/assets/7e25ee53-00f7-4af0-af1f-b7a38c122820)

