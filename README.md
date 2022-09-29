# StockPriceApp

Приложение для получения информации и цены акции по ти́керу 
(англ. ticker – краткое название актива на бирже) через [telegram-бота](https://telegram.me/StocksPricesBot),
либо через API. Реализовано на основке SDK [Tinkoff Invest API](https://tinkoff.github.io/investAPI/). <br> 
Этот pet-проект служит для демонстрации микросервисной архитектуры на актуальном стеке технологий.


### Стек технологий
- Java 11
- Spring Boot
- Spring Kafka
- Spring Data JPA/JDBC
- Spring Web
- Spring Cache (Caffeine)
- [Dotenv for Spring](https://github.com/paulschwarz/spring-dotenv) (support .env files)
- Lombok
- MapStruct
- [TelegramBots](https://github.com/rubenlagus/TelegramBots/tree/master/telegrambots-spring-boot-starter) (Spring Boot starter)
- Java SDK for **[Tinkoff Invest API](https://github.com/Tinkoff/invest-api-java-sdk)**
- Swagger (OpenAPI 3.0)
- Postgresql (+PostGis)
- Flyway
- Gradle
- Docker
- Kubernetes (Kind)
- GitLab CI (GitLab-Runner locality) + Github Actions 

## Сборка приложения и деплой
**Необходимо:**
1. Java 11+
2. Docker
3. Kubernetes
4. Kind (Locally cluster)
5. GitLab-Runner

### Установка kind
```shell script
# запустить под админом cmd.exe 
# создать папку для 'kind'
mkdir C:\kind
# Установить kind на Windows 64bit (Для других ОС https://kind.sigs.k8s.io/docs/user/quick-start/#installing-from-release-binaries)
curl -Lo C:\kind\kind.exe https://kind.sigs.k8s.io/dl/v0.11.1/kind-windows-amd64 --ssl-no-revoke
# установить kind в системную переменную %PATH%. WARNING: This solution may be destructive to your PATH
setx /M path "%PATH%;C:\kind\ "
# подключение плагина Ingress NGINX
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

### Установка gitlab-runner
```shell script
# создать папку для 'gitlab-runner'
mkdir C:\GitLab-Runner
# Установить GitLab-Runner на Windows 64bit
curl -Lo C:\GitLab-Runner\gitlab-runner.exe https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-windows-amd64.exe --ssl-no-revoke
# установить gitlab-runner в системную переменную %PATH%. WARNING: This solution may be destructive to your PATH
setx /M path "%PATH%;C:\GitLab-Runner\ "
cd C:\GitLab-Runner
# запуск установки gitlab-runner
gitlab-runner install
# регистрация gitlab-runner
gitlab-runner register --non-interactive --name <НАЗВАНИЕ> --url https://gitlab.com/ --registration-token <ТОКЕН РЕПОЗИТОРИЯ ИЗ GITLAB>  --executor shell --shell <powershell|pwsh>
# запуск сервиса gitlab-runner
gitlab-runner start
# проверка запущен ли gitlab-runner
gitlab-runner status
```

### Сборка без CI/CD
```shell script
# Склонировать проект к себе
git clone https://github.com/devalurum/stockPriceApp.git

# создать файл gradle.properties и добавить credentials от dockerhub
# 'DOCKERHUB_USERNAME' и 'DOCKERHUB_PASSWORD'
# создать файл .env в модуле telegram-stock-bot:
# 'resources/config/.env' с переменной TELEGRAM_TOKEN=<ВАШ_ТОКЕН_ОТ_TELEGRAM_БОТА>
# создать файл .env в модуле tinkoff-stock-app в ресурсах:
# 'resources/config/.env' с переменной TINKOFF_TOKEN=<ВАШ_ТОКЕН_ОТ_TINKOFF_API>
# нужно указать свой никнейм в docker-hub в тасках jib для публикации  

# Сборка (build), упаковка в docker-контейнеры и публикация в docker-hub
gradlew jib

# Создать кластер
kind create cluster --config=kubernetes/kind-config.yaml
# перейти в папку kubernetes
cd kubernetes

# Импорт общей конфигураций
kubectl apply -f config/stock-app-config.yaml
# Деплой kafka + zookeeper
kubectl apply -f kafka/zookeeper.yaml
kubectl apply -f kafka/kafka.yaml
# Деплой PostgreSQL + PostGIS
kubectl apply -f db/postgres-config.yaml
kubectl apply -f db/storage_postgres.yaml
kubectl apply -f db/postgres.yaml
# Деплой микросервиса с подсчётом статистики
kubectl apply -f stock-app-statistic/service_stock-app-stat.yaml
kubectl apply -f stock-app-statistic/deploy_stock-app-stat.yaml
# Деплой микросервиса с телеграм-ботом
kubectl apply -f telegram-stock-bot/service_telegram-stock-bot.yaml
kubectl apply -f telegram-stock-bot/deploy_telegram-stock-bot.yaml
# Деплой микросервиса с tinkoff API
kubectl apply -f tinkoff-stock-app/service_tinkoff-stock-app.yaml
kubectl apply -f tinkoff-stock-app/ingress_tinkoff-stock-app.yaml
kubectl apply -f tinkoff-stock-app/deploy_tinkoff-stock-app.yaml
# go to telegram bot or http://localhost:8880/tinkoff-stock-app/swagger-ui/index.html
```

## Todo:
- Написать тесты. Попробовать Testcontainers для тестирования kafka и 
для репозиториев с помощью H2Gis или PostgreSQL с расширением PostGis.
- Придумать единую точку сбора ошибок (на подобии @RestControllerAdvice) для kafka и отправке юзеру.
- Реализовать логирование и мониторинг с помощью Grafana Loki/Prometheus + Spring Actuator.
- Переделать кэширование с дефолтого Spring Caching + Caffeine, на Redis.
- Решить конликт в Swagger UI с портами.
- Рефакторинг.
- \* Попробовать (необязательно) настроить маппинг сообщений через kafka не через ObjectMapper,
а с помощью сопоставления типов.
