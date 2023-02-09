FROM java:8

COPY *.jar /daily.jar

CMD ["--server.port=8080"]

EXPOSE 8080

ENTRYPOINT ["java","-jar","/daily.jar"]