package ru.devalurum.telegramstockbot.telegrambot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.exception.TelegramBotException;
import ru.devalurum.telegramstockbot.service.UserService;
import ru.devalurum.telegramstockbot.service.kafka.KafkaProducerService;
import ru.devalurum.telegramstockbot.settings.TelegramSettings;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramAnswerStrategy;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramCommand;
import ru.devalurum.telegramstockbot.utils.Constants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramSettings settings;
    private final KafkaProducerService producerService;
    private final Map<TelegramCommand, TelegramAnswerStrategy> strategyMap;
    private final UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            // Обработка команд, начинающихся со слэша
            if (message.getText().startsWith("/")) {
                String command = message.getText().replaceFirst("/", "");
                var strategy = strategyMap.get(TelegramCommand.getCommandByText(command));
                strategy.sendAnswer(message, this);
            } else if (message.getText().equals(Constants.SEND_LOCATION)) {
                // Отправка местоположения с веб-клиента не работает, к сожалению
                sendResponse(message.getChatId(), Constants.ERROR_FROM_WEB_SEND_LOCATION, message.getMessageId());
            } else {
                // Обработка запроса с тикером
                producerService.sendMessage(message);
            }
        } else if (update.getMessage().getLocation() != null) {
            log.info("location from message: {}", update.getMessage().getLocation());
            userService.update(update.getMessage());
        }
    }

    public void sendResponse(long chatId, String textToSend) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(textToSend)
                .build();

        try {
            this.execute(message);
            log.info("Sent message \"{}\" to {}", textToSend, chatId);
        } catch (TelegramApiException e) {
            log.error("Failed to send message \"{}\" to {} due to error: {}", textToSend, chatId, e.getMessage());
            throw new TelegramBotException(e);
        }
    }

    public void sendResponse(long chatId, String textToSend, String parseMode) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(textToSend)
                .parseMode(parseMode)
                .build();

        try {
            this.execute(message);
            log.info("Sent message \"{}\" to {}", textToSend, chatId);
        } catch (TelegramApiException e) {
            log.error("Failed to send message \"{}\" to {} due to error: {}", textToSend, chatId, e.getMessage());
            throw new TelegramBotException(e);
        }
    }

    public void sendResponse(long chatId, String textToSend, int messageIdToReply) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(textToSend)
                .replyToMessageId(messageIdToReply)
                .parseMode(ParseMode.HTML)
                .build();

        try {
            this.execute(message);
            log.info("Sent message \"{}\" to {}", textToSend, chatId);
        } catch (TelegramApiException e) {
            log.error("Failed to send message \"{}\" to {} due to error: {}", textToSend, chatId, e.getMessage());
            throw new TelegramBotException(e);
        }
    }

    public void sendResponseWithLocation(long chatId, String textToSend) {
        SendMessage sendGeolocation = SendMessage.builder()
                .text(textToSend)
                .chatId(String.valueOf(chatId))
                .replyMarkup(getKeyboardWithRequestLocationAndContact())
                .build();

        try {
            Message message = this.execute(sendGeolocation);
            log.info("location: {}", message.getLocation());
        } catch (TelegramApiException e) {
            log.error("Error setting button: " + e.getMessage());
        }
    }

    private ReplyKeyboard getKeyboardWithRequestLocationAndContact() {
        KeyboardButton locationButton = KeyboardButton.builder()
                .text(Constants.SEND_LOCATION)
                .requestLocation(true)
                .build();

        KeyboardButton contactButton = KeyboardButton.builder()
                .text(Constants.SEND_CONTACT)
                .requestContact(true)
                .build();

        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.addAll(List.of(locationButton, contactButton));

        return ReplyKeyboardMarkup.builder()
                .oneTimeKeyboard(true)
                .keyboardRow(keyboardButtons)
                .selective(true)
                .resizeKeyboard(true)
                .build();
    }

    @PostConstruct
    private void initCommands() {
        List<BotCommand> botCommandList = Arrays.stream(TelegramCommand.values())
                .filter(command -> command != TelegramCommand.NOT_FOUND)
                .map(command -> new BotCommand(Constants.FORWARD_SLASH + command.getCommandAsString(),
                        command.getDescription()))
                .collect(Collectors.toList());

        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return settings.getUsername();
    }

    @Override
    public String getBotToken() {
        return settings.getToken();
    }
}
