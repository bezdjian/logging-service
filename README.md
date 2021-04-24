# Message logging service

**Installation instructions**

<ul>
<li>Install maven and edit the PATH and MAVEN_HOME</li>

<li>
Clone or download the project

	git clone https://github.com/bezdjian/message-logging-service.git

</li>

<li>
Cd into project root where pom.xml file resides and run mvn install

	cd message-logging-service
	mvn clean install

**- This will compile, run unit tests and spring boot tests.**
</li>

<li>
Run mvn **integration tests** with

    mvn clean install -Pintegration-test

**- This will run all the tests plus an integration test towards the service**
</li>

<li>
Run the application:

	java -jar target/message-logging-service-0.1.jar

- This will run the application together with remove old messages scheduler default of 1 minute interval, when maxAge is
  set, then older messages will be deleted from the database

</li>

<li>
Run the application with custom scheduler interval:

    java -jar target/message-logging-service-0.1.jar --cronjob.removeOldMessages.interval=5000

- Where 5000 is 5 seconds in milliseconds

</li>

<li>
Access the Swagger for REST API documentation

<a>http://localhost:8080/message-logging-service/swagger-ui.html </a>
</li>

<li>
Access H2 in-memory database console during application runtime

<a>http://localhost:8080/message-logging-service/h2-console </a>

- Replace database URL (JDBC URL) to *jdbc:h2:mem:test*
- Username: sa
- Password: < leave empty >
- Click Connect

</li>
</ul>