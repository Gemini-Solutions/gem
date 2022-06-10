#!/bin/bash
APP_NAME=gembook-service.jar
if ps -ef |grep -v grep | grep $APP_NAME > /dev/null
then
    cmd="kill `ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}'`"
    echo "Stopping service - Running :$cmd"
    eval $cmd
    sleep 7
    if ps -ef |grep -v grep | grep $APP_NAME > /dev/null
    then
        cmd="kill -9 `ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}'`"
        echo "Not stopped - Running :$cmd"
        eval $cmd
    else
        echo "stopped successfully"
    fi
else
    echo "no running instance found to kill"

fi