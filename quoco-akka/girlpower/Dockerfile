FROM openjdk:8-jre-alpine
ENV HOSTNAME="http://girlpower_ws:9002/quotation?wsdl"
COPY target/girlpower-1.0-jar-with-dependencies.jar /girlpower.jar
CMD /usr/bin/java -jar /girlpower.jar "$HOSTNAME"
