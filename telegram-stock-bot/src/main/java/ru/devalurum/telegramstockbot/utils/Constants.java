package ru.devalurum.telegramstockbot.utils;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {

    public static final String STRING_EMPTY = "";

    public static final String FORWARD_SLASH = "/";

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN_RU = "HH:mm:ss dd.MM.yyyy";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static final String WRONG_REQUEST = "К сожалению, такой команды не существует.";

    public static final String ANSWER_AFTER_START = "Привет, %s, это тестовый бот для получения цены акции по тикеру!" + " :blush:";

    public static final String DELETE_INFO = "Вся информация о вашем аккаунте была удалена.";
    public static final String INFO_NOT_STORED = "Информация о вашем аккаунте не хранится.";

    public static final String SEND_LOCATION = "Предоставить местоположение";
    public static final String SEND_CONTACT = "Предоставить контакт";

    public static final String ERROR_FROM_WEB_SEND_LOCATION = "Данная функция не поддерживается с веб-клиента.";

    public static final String GOOGLE_MAPS_URL = "https://www.google.ru/maps/?q=%s,%s&z=18";

    public static final String HELP_TEXT = "Этот бот создан для демонстрации микросервисной архитектуры на актуальном" +
            "стеке технологий: Java, Spring boot, Kafka, Kubernetes, GitLab CI/CD.\n\n" +
            "Введите ти́кер (краткое название) какой-либо акции, чтобы узнать её текующую цену.\n" +
            "Также, Вы можете запустить команды из меню или в выпадающем списке:\n\n" +
            "Введите /start, чтобы активировать бота.\n\n" +
            "Введите /mydata, чтобы увидеть информацию о себе.\n\n" +
            "Введите /deletedata, чтобы удалить информацию о себе.\n\n" +
            "Введите /botstat, чтобы увидеть информацию о статистике бота.\n\n" +
            "Введите /techstack, чтобы увидеть стек используемых технологий и контакты разработчика.\n\n" +
            "Введите /help, чтобы увидеть это сообщение ещё раз.";

}
