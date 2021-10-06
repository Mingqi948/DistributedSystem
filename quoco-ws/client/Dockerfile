FROM openjdk:8-jre-alpine
ENV HOSTNAME="http://localhost:9000/broker?wsdl"
COPY target/client-1.0-jar-with-dependencies.jar /client.jar
CMD /usr/bin/java -jar /client.jar "$HOSTNAME"
