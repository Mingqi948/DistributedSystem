<h1>To run the project on one computer:</h1>
You may need to as many terminals as desired and make sure you run the client after Broker Service established.

Run:
````
mvn install
````

Then, compile each module (client module must be executed at last as it depends on broker & other services):
````
mvn compile exec:java -pl <module_name>
````
<br><br>
<h1>Run with docker-compose</h1>
You can simply use this command in root folder:

````
mvn package
docker-compose up
````
Also, you can just execute [./run.sh](run.sh) scripts.<br>
<h3>Cautions</h3>
After run.sh scripts running. You can press Ctrl + C to quit. And it will help you to stop/remove containers & images that are generated in early pahse.
