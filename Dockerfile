FROM openjdk:8-jdk-alpine
LABEL maintainer="www.rationalminds.net"
RUN addgroup -S pluto && adduser -S pluto -G pluto -h /home/pluto
USER pluto
COPY --chown=pluto:pluto LogSerach/target/pluto.war /home/pluto/
ADD http://rationalminds.net/download/envswitch/0.0.2/environment-switch-0.0.2-jar-with-dependencies.jar /home/pluto
USER pluto:pluto
ENTRYPOINT ["java","-jar","/home/pluto/pluto.war"]
EXPOSE 8080

