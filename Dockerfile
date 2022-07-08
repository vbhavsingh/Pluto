FROM openjdk:8-jdk-alpine
LABEL maintainer="www.rationalminds.net"
RUN addgroup -S pluto && adduser -S pluto -G pluto -h /home/pluto
USER root
ADD http://rationalminds.net/download/pluto/5.0/pluto.war /home/pluto/
ADD http://rationalminds.net/download/envswitch/0.0.2/environment-switch-0.0.2-jar-with-dependencies.jar /home/pluto
RUN chmod 777 /home/pluto/pluto.war
RUN chmod 777 /home/pluto/environment-switch-0.0.2-jar-with-dependencies.jar
USER pluto:pluto
ENTRYPOINT ["java","-jar","/home/pluto/pluto.war"]
EXPOSE 8080

