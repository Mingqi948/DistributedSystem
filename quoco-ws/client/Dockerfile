FROM openjdk:15
ENV HOSTNAME="http://172.22.0.5/broker?wsdl"
COPY target/client-1.0-jar-with-dependencies.jar /client.jar
CMD /usr/bin/java -jar /client.jar $HOSTNAME
