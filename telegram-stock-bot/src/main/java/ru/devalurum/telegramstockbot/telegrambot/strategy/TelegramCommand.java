package ru.devalurum.telegramstockbot.telegrambot.strategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.devalurum.telegramstockbot.utils.Utils;

@RequiredArgsConstructor
@Getter
public enum TelegramCommand {
    START("start", "приветственное сообщение и активация"),
    HELP("help", "вспомогательное сообщение"),
    MY_DATA("mydata", "вывести данные пользователя"),
    DELETE_DATA("deletedata", "удалить данные пользователя"),
    BOT_STAT("botstat", "посмотреть статистику использования телеграм-бота"),
    TECH_STACK("techstack", "посмотреть стек используемых технологий"),
    NOT_FOUND("", "");

    private final String commandAsString;
    private final String description;

    public static TelegramCommand getCommandByText(String text) {
        for (TelegramCommand command : values()) {
            if (command.getCommandAsString().equalsIgnoreCase(text) ||
                    Utils.containsIgnoreCase(command.getCommandAsString(), text)) {
                return command;
            }
        }

        return NOT_FOUND;
    }
}
