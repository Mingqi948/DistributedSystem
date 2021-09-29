<h1>To run the project on one computer:</h1>
You will need to open 5 terminals and make sure you run the client after Broker Service established.

Run:
````
mvn clean install
````

Terminal #1: 
````
cd auldfellas
mvn compile exec:java
````

Terminal #2:
````
cd girlpower
mvn compile exec:java
````

Terminal #3:
````
cd dodgydrivers
mvn compile exec:java
````

Terminal #4:
````
cd broker
mvn compile exec:java
````

Terminal #5:
````
cd client
mvn compile exec:java
````

<h3>Cautions</h3>

<ul>
<li>If client is executed without the Broker services established. Client will detect and terminate.</li>
<li>If Broker service is established while some quotation services are not available. The program can still execute as 
normal but output won't include all information.</li>
<li>During the execution, if one service is interrupted(CTRL C), the other will require to be restarted.</li>
</ul>



<h1>Run tests</h1>

In root folder:
````
mvn clean test
````

<h1>Run with docker-compose</h1>
You can simply use this command in root folder:

````
docker-compose up
````
Also, you can just execute [./run.sh](run.sh) scripts. But be very cautious when using this scripts as it REMOVE all images and containers everytime executed!!!
