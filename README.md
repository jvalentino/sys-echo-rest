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

This is the easiest place to start because it doesn't involve security. We just rename HomeModel to HomeDto, and have that be the result object.

```groovy
@Controller
@Slf4j
@RestController
@CompileDynamic
class HomeRest {

    @Autowired
    UserService userService

    @Autowired
    DocService docService

    @GetMapping('/')
    HomeDto index() {
        log.info('Rendering index')
        HomeDto response = new HomeDto()
        response.with {
            users = userService.countCurrentUsers()
            documents = docService.countDocuments()
        }

        response
    }

}
```

The end result of http://localhost:8080 is then:

```json
{
   "users":1,
   "documents":1
}
```

### /custom-login

We are just returning a general Result (true or false), which when false contains a general error message like "invalid credentials":

```groovy
@Controller
@Slf4j
@RestController
@CompileDynamic
class LoginRest {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserService userService

    @PostMapping('/custom-login')
    ResultDto login(@RequestBody UserDto user) {
        userService.login(user, authenticationManager)
    }

}
```

It is recommended you use Postman to test it, but you can also just use curl:

```bash
curl -v --location --request POST 'http://localhost:8080/custom-login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email":"admin",
    "password": "6c8ec11f-9f8e-4b9d-b861-c2eebec81b34"
}'
```

In the result, we will later need the session information that is given in the header:

```bash
   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /custom-login HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.79.1
> Accept: */*
> Content-Type: application/json
> Content-Length: 79
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Set-Cookie: SESSION=ZGUxMGFlOTYtZDEyNS00ZmU5LTlkZTQtYjg1NDgyMDBhM2Vj; Path=/; HttpOnly; SameSite=Lax
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 20 Feb 2023 17:57:11 GMT
< 
* Connection #0 to host localhost left intact
{"success":true,"message":null}
```

In this case:  SESSION=ZGUxMGFlOTYtZDEyNS00ZmU5LTlkZTQtYjg1NDgyMDBhM2Vj



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

To hit this though, you now need to be logged in which you means you have to provide the session token:

```bash
curl -v --location --request GET 'http://localhost:8080/dashboard' \
--header 'Content-Type: application/json' \
--header 'Cookie: SESSION=ZGUxMGFlOTYtZDEyNS00ZmU5LTlkZTQtYjg1NDgyMDBhM2Vj'
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

 With the result now being:

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
            "firstName":"admin",
            "lastName":"admin"
         },
         "updatedByUser":{
            "authUserId":1,
            "email":"admin",
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









