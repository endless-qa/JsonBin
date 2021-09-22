### Overview
This project contains a basic set of tests for the CRUD operations of the JsonBin API.
For the sake of simplicity and time balance, some flows are not included (for example, 403 error cases).

### How to run
All tests in the project can be run using the following command: ``mvn clean test -DapiToken=[YOUR_API_TOKEN]``.
This is done with the purpose to prevent leaking secrets.

Please **pay attention** that a JsonBin API token can contain characters that must be escaped when
placing them into command-line. Use online conversion tools like https://onlinelinuxtools.com/escape-shell-characters for the proper escaping.
Otherwise, a non-escaped token will be treated as a totally different (and probably non-existing) token, and test execution will fail.

In case of executing tests via IDE, `-DapiToken=[YOUR_API_TOKEN]` can be set in the editing run configuration dialogue > Build & Run.
Escaping a token is not needed in thi case.

A simple GitLab CI template is also added to the project to make tests execution faster in the pipeline, if needed.

### Observations and remarks
- Some tests fail because the server returns different error codes and messages than documented. I treat them as a valid bug
as the changes have to be reflected in the API specification, but they're not.

- For the sake of demo, there are 2 different approaches used in coding tests: **plain** (traditional) with using soft assertions and **BDD-like** using ValidatableResponse.

- The framework will throw an exception, if an API token is not set or sett incorrectly. This is done with the purpose, as there is not much sense in the test execution if it's not authorised.

- Some tricks could be done better (for ex, serializing and deserializing content into a Pair) but that is quite time-consuming