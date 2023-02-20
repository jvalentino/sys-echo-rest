# System Echo (REST)



# Architecture

## Changes

Project renaming import

- settings.gradle - project name
- Docker-compose.yaml - rename containers to echo
- docker-compose-system.yaml - rename containers to echo (Tomcat containers will go away)
- Rename DeltaApp to EchoApp
- Rename delta package to echo under src and test
- SpringWebConfig - rename path to echo
- DocVersionRepo - Rename class HQL to echo
- Run `./gradlew check` to ensure all unit/integration tests pass

Now it is time to verify everything still works:

- `docker compose up -d`
- Connect to database in pgadmin, create new database called `examplesys`
- Create new Gradle Application runtime in IntelliJ that runs `tomcatRunWar`
- Run the system using `./gradlew tomcatRunWar`, and catch the admin password in the logs
- Go http://localhost:8080 and login, upload a document, and then view versions
- 





