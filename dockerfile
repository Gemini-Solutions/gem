FROM anapsix/alpine-java
MAINTAINER vaishali.jain@geminisolutions.in
COPY target/gembook-service.jar /home/
WORKDIR /home/
EXPOSE 7000
ENTRYPOINT ["java","-jar","gembook-service.jar"]