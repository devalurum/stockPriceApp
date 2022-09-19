package ru.devalurum.stockappstat.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import ru.devalurum.stockappstat.domain.dto.MessageShortDto;
import ru.devalurum.stockappstat.services.StatService;
import ru.devalurum.stockappstat.services.TelegramRequestStatisticsProcessor;

import java.util.concurrent.atomic.AtomicLong;


@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramRequestStatisticsProcessorImpl implements TelegramRequestStatisticsProcessor, InitializingBean {

    private final AtomicLong requestCounter = new AtomicLong(0);
    private final StatService statService;

    @Override
    public void processMessage(MessageShortDto message) {
        log.info("message:{}", message);
        var lastValue = requestCounter.incrementAndGet();
        statService.save(lastValue);
        log.info("current requestCounter:{}", lastValue);
    }

    @Override
    public long getRequestCounter() {
        return requestCounter.get();
    }

    @Override
    public void afterPropertiesSet() {
        if (statService.getStat().isPresent()) {
            var currentValue = statService.getStat().get().getCountRequests();
            requestCounter.set(currentValue);
        }
    }
}
