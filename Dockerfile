FROM tomcat:9.0
LABEL maintainer="www.rationalminds.net"
RUN rm -rf /usr/local/tomcat/webapps/
RUN wget -P /usr/local/tomcat/webapps/ https://rationalminds.net/download/pluto/4.0/pluto.war
RUN mkdir /usr/local/tomcat/agents
RUN wget -P /usr/local/tomcat/agents/ https://rationalminds.net/download/envswitch/0.0.2/environment-switch-0.0.2-jar-with-dependencies.jar
ENV JAVA_OPTS="$JAVA_OPTS -javaagent:/usr/local/tomcat/agents/environment-switch-0.0.2-jar-with-dependencies.jar -Denv.switch=dev"
EXPOSE 8080
