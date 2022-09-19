FROM amazoncorretto:11-alpine3.12
COPY build/libs/tinkoffStockApp.jar /

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "tinkoffStockApp.jar"]
EXPOSE 8083