# Wolt Backend Assignment

## How to run

### On IntelliJ IDEA

Open the project. The IDE should ask to load a gradle file which will set up everything nicely. Then, you can run the API by running the *main* function located in the `BackendAssignmentWoltApplication.kt` file. It should launch without any problem and you would then be able to test some HTTP requests that I wrote in the `requests.http` file using the embedded HTTP Client.

Here is how running the project should go.

![Running the project using IntelliJ](/Media/intellij%20run.PNG "Running the project using IntelliJ")

And here is what running the tests should look like.

![Gradle demo](/Media/http%20requests%20intellij.PNG "Running the HTTP Requests on IntelliJ")

I have generated an HTML file containing all the outputs for the tests (altough less clearly than using the HTTP CLient).

### Using the terminal and an API testing tool like Postman

Simply verify that your `JAVA_HOME` environment variable is set up to a JDK, then run the following command :

```bash
gradlew bootrun
```

At this point, you should get the same result as the image below.

![Gradle demo](/Media/gradle%20demo.PNG "Gradle demo")

To test the API, I decided to use Postman, a tool that is quite useful in cases like these.

I only include the first test present in `requests.http`, but the rest can be replicated easily.

![Postman example](/Media/postman_example.PNG "Postman example")

Testing scripts can be implemented in Postman as well to verify the response data, like I did with the IntelliJ IDEA HTTP Client.
