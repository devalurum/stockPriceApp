package ru.devalurum.stockappstat.services;

import ru.devalurum.stockappstat.domain.dto.MessageShortDto;

public interface TelegramRequestStatisticsProcessor {

    void processMessage(MessageShortDto message);

    long getRequestCounter();

}
