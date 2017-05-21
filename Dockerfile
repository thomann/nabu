FROM tomcat:8.0-alpine

RUN echo org.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false >> $CATALINA_HOME/conf/catalina.properties

COPY webutil/ $CATALINA_HOME/webapps/nabutil
COPY web/ $CATALINA_HOME/webapps/nabu

# Something like VOLUME $CATALINA_HOME/webapps/nabu/WEB-INF/uploadvocs/ ???

EXPOSE 8080

CMD ["catalina.sh","run"]