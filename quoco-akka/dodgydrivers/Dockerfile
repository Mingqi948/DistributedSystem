FROM openjdk:8-jre-alpine
ENV HOSTNAME="http://dodgydrivers_ws:9003/quotation?wsdl"
COPY target/dodgydrivers-1.0-jar-with-dependencies.jar /dodgydrivers.jar
CMD /usr/bin/java -jar /dodgydrivers.jar "$HOSTNAME"