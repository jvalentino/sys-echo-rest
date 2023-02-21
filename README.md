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

### /view/versions

Same pattern, name the model class to a DTO, and then return it as the method result:

```groovy
@GetMapping('/view-versions/{docId}')
    ViewVersionDto index(@PathVariable(value='docId') Long docId) {
        ViewVersionDto result = new ViewVersionDto()
        result.with {
            doc = docService.retrieveDocVersions(docId)
        }

        log.info("Doc ${docId} has ${result.doc.versions.size()} versions")

        result
    }
```

cURL can be used to invoke the service:

```bash
curl -v --location --request GET 'http://localhost:8080/view-versions/2' \
--header 'Content-Type: application/json' \
--header 'Cookie: SESSION=ZGUxMGFlOTYtZDEyNS00ZmU5LTlkZTQtYjg1NDgyMDBhM2Vj'
```

Resulting in:

```json
{
    "doc": {
        "docId": 2,
        "name": "sample.pdf",
        "mimeType": "application/pdf",
        "createdByUser": null,
        "updatedByUser": null,
        "createdDateTime": 1676902931940,
        "updatedDateTime": 1676902931940,
        "versions": [
            {
                "docVersionId": 3,
                "versionNum": 1,
                "doc": null,
                "data": null,
                "createdDateTime": 1676902931940,
                "createdByUser": null
            }
        ],
        "tasks": null
    }
}
```

### /download/version

This returns binary data, and doesn't have to be changed at all:

```groovy
// https://www.baeldung.com/servlet-download-file
    @GetMapping('/version/download/{docVersionId}')
    void downloadVersion(@PathVariable(value='docVersionId') Long docVersionId, HttpServletResponse response) {
        DocVersion version = docService.retrieveVersion(docVersionId)

        response.setContentType(version.doc.mimeType)
        response.setHeader('Content-disposition',
                "attachment; filename=${version.doc.name.replaceAll(' ', '')}")

        InputStream is = new ByteArrayInputStream(version.data)
        OutputStream out = response.getOutputStream()

        byte[] buffer = new byte[1048]

        int numBytesRead
        while ((numBytesRead = is.read(buffer)) > 0) {
            out.write(buffer, 0, numBytesRead)
        }
    }
```

If you invoke http://localhost:8080/version/download/3 via postman, you can see the PDF in the body instead of having to worry about it with cURL.

### /version/new *

Same pattern as before, no more redirect and just return the ResultDto:

```groovy
 @PostMapping('/version/new/{docId}')
    ResultDto upload(@RequestParam('file') MultipartFile file, @PathVariable(value='docId') Long docId) {
        AuthUser user = userService.currentLoggedInUser()

        docService.uploadNewVersion(user, file, DateGenerator.date(), docId)

        new ResultDto()
    }
```

However, this can be quite involved with trying to get it work through cURL or Postman, so we are going to wait to verify it when the UI is hooked up.

### /upload-file *

Same pattern as before, no more redirect and just return the ResultDto:

```groovy
@Controller
@Slf4j
@RestController
@CompileDynamic
class UploadRest {

    @Autowired
    DocService docService

    @Autowired
    UserService userService

    @PostMapping('/upload-file')
    ResultDto upload(@RequestParam('file') MultipartFile file) {
        AuthUser user = userService.currentLoggedInUser()
        docService.uploadNewDoc(user, file, DateGenerator.date())

        new ResultDto()
    }

}
```

However, this can be quite involved with trying to get it work through cURL or Postman, so we are going to wait to verify it when the UI is hooked up.

### CORS

```
Access to fetch at 'http://localhost:8080//' from origin 'http://localhost:3000' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource. If an opaque response serves your needs, set the request's mode to 'no-cors' to fetch the resource with CORS disabled.
```

TBD

### Login Revisited



```groovy
@Bean
    HttpSessionIdResolver httpSessionIdResolver() {
        HeaderHttpSessionIdResolver.xAuthToken()
    }
```





