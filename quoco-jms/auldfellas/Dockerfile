FROM openjdk:8-jre-alpine
ENV HOST="auldfellas_jms"
COPY target/auldfellas-1.0-jar-with-dependencies.jar /auldfellas.jar
CMD /usr/bin/java -jar /auldfellas.jar "$HOST"
