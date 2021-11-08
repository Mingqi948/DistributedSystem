FROM openjdk:8-jre-alpine
ENV HOSTNAME="http://auldfellas_ws:9001/quotation?wsdl"
COPY target/auldfellas-1.0-jar-with-dependencies.jar /auldfellas.jar
CMD /usr/bin/java -jar /auldfellas.jar "$HOSTNAME"
