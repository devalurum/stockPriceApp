FROM amazoncorretto:11-alpine3.12
COPY build/libs/telegramStockBot.jar /

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "telegramStockBot.jar"]
EXPOSE 8081