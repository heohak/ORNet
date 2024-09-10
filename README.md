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
Just "./gradlew build" in the terminal/console

### Docker 
You can download Docker Desktop here [Docker Desktop](https://www.docker.com/) for a GUI applicaton or just [Engine](https://docs.docker.com/engine/install/) and run the commands in cmd

### Postman (Optional)
For endpoint testing you can download Postman here [Postman](https://postman.com/)

## For Local Development Setup Instructions

    git clone https://github.com/heohak/ORNet.git
    cd heohak/ORNet
    
- Or when Using IDEs just press "Get From VCS" and put the same URL: https://github.com/heohak/ORNet.git
- Use Gradle to build the project and download dependencies:

        ./gradlew clean build
    
- Or in the IDE you can also press gradle on the right side and then Tasks/build/build
- Make sure Docker is installed and running on your machine. Then, create and start a PostgreSQL container:

        docker run --name postgres-container -e POSTGRES_USER=youruser -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=yourdb -p 5432:5432 -d postgres
  
- Change the application properties if you are using some other database container than postgre and change the password and username accordingly
- And run the program (the database container has to be running also)

## Deployment
*Coming Soon*

## Project Structure

- `src/`
    - `main/java/com/demo/bait/`: Contains the source code for the backend app.
        - `components/`: Software table components.
        - `config/`: Configuration file for CORS.
        - `controller/`: Rest controllers for every entity.
        - `converter/`: Converters.
        - `dto/`: DTOs for entities.
        - `entity/`: All tables are generated based on entities.
        - `mapper/`: Mappers to map entities to DTOs.
        - `repository`: All repositories for entities.
        - `service/`: All endpoints functionality.
        - `specification/`: Search functions for entities.
    - `resources/`: Contains application properties and database config files.
  
## Database Diagram
![Bait_Partner_Project_Diagram_1](https://github.com/user-attachments/assets/1dc0792a-85bd-4dde-b08c-6d90679024c4)

