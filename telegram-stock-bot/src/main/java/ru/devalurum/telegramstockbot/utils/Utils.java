package ru.devalurum.telegramstockbot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Location;
import ru.devalurum.telegramstockbot.domain.dto.StatDto;
import ru.devalurum.telegramstockbot.domain.dto.StockDto;
import ru.devalurum.telegramstockbot.domain.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Currency;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@UtilityClass
public class Utils {

    public static String stockAsStringMessageForTelegram(StockDto stockDto) {
        Currency cur = Currency.getInstance(stockDto.getCurrency());
        String out = "Акция: <b>" + stockDto.getName() + "</b>\n" +
                "Цена: <b>" + stockDto.getPrice().setScale(2, RoundingMode.HALF_UP) + cur.getSymbol() + "</b>\n" +
                "Биржа: <b>" + stockDto.getExchange() + "</b>\n" +
                "Источник: <b>" + stockDto.getSource() + "</b>\n" +
                "Время: <b>" + stockDto.getTime().format(Constants.DATE_TIME_FORMATTER) + "</b>";

        return out;
    }

    public static String statAsStringMessageForTelegram(StatDto statDto) {
        if (statDto.getStartTime() == null && statDto.getUpdateTime() == null) {
            return "Всего запросов к <i>Tinkoff API</i> через бота: <b>" + 0 + "</b>.\n";
        }

        long daysBetween = DAYS.between(statDto.getStartTime(), statDto.getUpdateTime());

        String out = "Всего запросов к <i>Tinkoff API</i> через бота: <b>" + statDto.getCountRequests() + "</b>\n" +
                "Последний запрос был <b>" + statDto.getUpdateTime().format(DateTimeFormatterWithPattern(
                Constants.DATE_PATTERN_RU)) + "</b>\n" +
                "<b>Дней</b> между первым и последним запросом: <b>" + daysBetween + "</b>";
        return out;
    }

    public static String userAsStringMessageForTelegram(UserDto userDto) {
        String out = "";

        if (userDto.getNickname() != null) {
            out += "Никнейм: <b>" + userDto.getNickname() + "</b>\n";
        }

        if (userDto.getFirstName() != null) {
            out += "Имя: <b>" + userDto.getFirstName() + "</b>\n";
        }

        if (userDto.getLastName() != null) {
            out += "Фамилия: <b>" + userDto.getLastName() + "</b>\n";
        }

        out += "Дата регистрации: <b>" + userDto.getRegisteredAt().format(Constants.DATE_TIME_FORMATTER) + "</b>\n";

        if (userDto.getCoordinates() != null) {
            Location loc = userDto.getCoordinates();
            out += "Геолокация: <b><a href=\"" + String.format(Constants.GOOGLE_MAPS_URL, loc.getLatitude(),
                    loc.getLongitude()) + "\">" + String.format("%s,%s", loc.getLatitude(), loc.getLongitude())
                    + EmojiParser.parseToUnicode(":round_pushpin:") + "</a></b>";
        }

        return out;
    }

    public static String convertStrBySplitWithCommaToUnmarkedList(@NotNull String str) {
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .map(s -> "• " + s + ".\n")
                .collect(Collectors.joining());
    }

    public static DateTimeFormatter DateTimeFormatterWithPattern(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }


    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
