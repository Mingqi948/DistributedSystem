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

<li>During the execution, if one service is interrupted (e.g. CTRL C), the other will require to be restarted.</li>
</ul>


<h1>Run with docker-compose</h1>
You can simply use this command in root folder:

````
docker-compose up
````
Also, you can just execute [./run.sh](run.sh) scripts.<br>
After scripts running. You can press Ctrl + C to quit. And it will help you to stop/remove containers & images that are generated in early pahse.
