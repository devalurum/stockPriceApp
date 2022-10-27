# StockPriceApp

Приложение для получения информации и цены акции по ти́керу 
(англ. ticker – краткое название актива на бирже) через [telegram-бота](https://telegram.me/StocksPricesBot),
либо через API. Реализовано на основе SDK [Tinkoff Invest API](https://tinkoff.github.io/investAPI/). <br> 
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
- Docker (+Gradle Jib)
- Kubernetes (KinD, Helm, Krew)
- GitLab CI (gitlab-runner locality with powershell) + Github Actions 

## Сборка приложения и деплой
**Необходимо:**
1. Java 11+
2. Docker
3. Kubernetes (kubectl)
4. Kind (Locally cluster)
5. Helm, krew
6. GitLab-Runner
7. \* Chocolatey (On Windows)

### Установка Chocolatey через PowerShell.exe или cmd.exe
```shell script
Set-ExecutionPolicy Bypass -Scope Process -Force; 
iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))

# либо через cmd.exe
@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "[System.Net.ServicePointManager]::SecurityProtocol = 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
```
### Установка Docker + Kubernetes CLI (kubectl), Helm и krew
```shell script
choco install docker-desktop -y
choco install kubernetes-cli -y # Либо из Docker-desktop в настройках можно включить Kubernetes
choco install krew -y
choco install kubernetes-helm -y
# установить helm в системную переменную %PATH%. 
setx /M path "%PATH%;%ALLUSERSPROFILE%\chocolatey\lib\kubernetes-helm\tools\windows-amd64\ "
```
### Установка kind (Kubernetes in Docker)
```shell script
# Если стоит пакетный менеджер Chocolatey (лучше установить чем вручную)
choco install kind -y

# Иначе запустить под админом cmd.exe 
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

# Если стоит Chocolatey 
choco install -y gitlab-runner --params="'/InstallDir=C:\GitLab-Runner /Service'" 

# Иначе скачиваем GitLab-Runner на Windows 64bit
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

## Todo:
- Добавить поиск акции по названию (а не только по тикеру).
- Написать тесты. Попробовать Testcontainers для тестирования kafka и 
для репозиториев с помощью H2Gis или PostgreSQL с расширением PostGis.
- Придумать единую точку сбора ошибок (на подобии @RestControllerAdvice) для kafka и отправке юзеру.
- Реализовать логирование и мониторинг с помощью Grafana Loki/Prometheus + Spring Actuator.
- Переделать кэширование с дефолтого Spring Caching + Caffeine, на Spring Caching + Redis.
- Решить конликт в Swagger UI с портами.
- Рефакторинг.
- \* Попробовать (необязательно) настроить маппинг сообщений через kafka не через ObjectMapper,
а с помощью сопоставления типов.
