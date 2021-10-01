FROM openjdk:11.0.8-jre
ENV HOSTNAME=localhost
COPY target/*-jar-with-dependencies.jar /service.jar
CMD java -jar /service.jar $HOSTNAME