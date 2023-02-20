# System Echo (REST)



# Architecture

## Changes

Project renaming and import:

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
- Commit it all, and verify that the pipeline still works

Now we are doing to take out all this JSP stuff:

- Delete the entire `controller` package
- Delete `MyServletInitializer`
- Delete `addResourceHandlers` and `viewResolver` from `SpringWebConfig`
- Delete a ton of stuff from build.gradle: war plugin, idea plugin, `spring-boot-starter-tomcat`, all the dependencies labeled `JSP Magic`
- From build.gradle, remove the gretty stuff
- Take `SpringBootServletInitializer` and `configure` out of EchoApp
- Refresh gradle dependencies in IntelliJ, and then run `./gradlew check`

At this point, we are going to replace one controller at a time with a REST endpoint instead. But first we can now run the application directly out of the IDE as a pure Spring Boot application.

### /

This is the easiest place to start because it doesn't involve security.



### /dashboard

We are creating `DashboardRest` and instead of `DashboardModel` we are using `DashboardDto`:

```groovy
@CompileDynamic
@RestController
@Slf4j
class DashboardRest {

    @Autowired
    DocService docService

    @GetMapping('/dashboard')
    DashboardDto dashboard() {
        log.info('Rendering dashboard')

        DashboardDto dashboard = new DashboardDto()
        dashboard.with {
            documents = docService.allDocs()
        }

        dashboard
    }

}
```

When running the main class you can now directly hit http://localhost:8080/dashboard and see a problem:

```json
{
   "documents":[
      {
         "docId":2,
         "name":"sample.pdf",
         "mimeType":"application/pdf",
         "createdByUser":{
            "authUserId":1,
            "email":"admin",
            "password":"$1$skh6IULO$AydCPNg.8MdflxOBShEmI0",
            "salt":"$1$skh6IULO",
            "firstName":"admin",
            "lastName":"admin"
         },
         "updatedByUser":{
            "authUserId":1,
            "email":"admin",
            "password":"$1$skh6IULO$AydCPNg.8MdflxOBShEmI0",
            "salt":"$1$skh6IULO",
            "firstName":"admin",
            "lastName":"admin"
         },
         "createdDateTime":1676902931940,
         "updatedDateTime":1676902931940,
         "versions":null,
         "tasks":null
      }
   ]
}
```

The hashed passwords are in the payload!

This is something you didn't have to worry about before, but now you do. You can just exclude that information form the JSON results:

```groovy
class AuthUser {

    @Id @GeneratedValue
    @Column(name = 'auth_user_id')
    Long authUserId

    String email

    @JsonIgnore
    String password

    @JsonIgnore
    String salt

```

 







