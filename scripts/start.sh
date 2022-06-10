#!/bin/bash
APP_NAME=gembook-service.jar
java -jar -Dspring.profiles.active=aws /home/centos/java-services/$APP_NAME > /dev/null 2> /dev/null < /dev/null &

if ps -ef |grep -v grep | grep $APP_NAME > /dev/null
then
   exit 0
else
   exit 1
fi