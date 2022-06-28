FROM openjdk:8u212-jre-slim-stretch
VOLUME /tmp
ARG JAR_FILE
ENV TZ Asia/Taipei
RUN apt-get update; apt-get install -y fontconfig libfreetype6
COPY entrypoint.sh entrypoint.sh
COPY lombok.jar lombok.jar
RUN chmod 755 /entrypoint.sh
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["/entrypoint.sh"]