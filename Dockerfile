FROM tomcat:9.0-slim
LABEL maintainer="www.rationalminds.net"
RUN rm -rf /usr/local/tomcat/webapps/
ADD https://rationalminds.net/download/pluto/4.0/pluto.war /usr/local/tomcat/webapps/
RUN mkdir /usr/local/tomcat/agents
ADD https://rationalminds.net/download/envswitch/0.0.2/environment-switch-0.0.2-jar-with-dependencies.jar /usr/local/tomcat/agents
ENV JAVA_OPTS="$JAVA_OPTS -javaagent:/usr/local/tomcat/agents/environment-switch-0.0.2-jar-with-dependencies.jar -Denv.switch=dev"
EXPOSE 8080
