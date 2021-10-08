FROM openjdk:8-jre-alpine
ENV HOSTNAME="broker_ws"
COPY target/broker-1.0-jar-with-dependencies.jar /broker.jar
CMD ["/usr/bin/java", "-jar", "/broker.jar", "$HOSTNAME"]