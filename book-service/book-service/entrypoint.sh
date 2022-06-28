#!/bin/sh

java $JAVA_OPTIONS -javaagent:/lombok.jar -Djava.security.egd=file:/dev/./urandom -jar /app.jar
