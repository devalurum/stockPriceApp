FROM openjdk:11-jre-slim-buster
COPY build/libs/stock-app-statistic.jar /

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "stock-app-statistic.jar"]
EXPOSE 8083