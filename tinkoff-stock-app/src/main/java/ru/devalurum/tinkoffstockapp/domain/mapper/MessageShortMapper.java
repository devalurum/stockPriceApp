package ru.devalurum.tinkoffstockapp.domain.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.devalurum.tinkoffstockapp.domain.dto.MessageShortDto;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;
import ru.devalurum.tinkoffstockapp.exception.MapperException;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ObjectMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MessageShortMapper {

    @Autowired
    private ObjectMapper mapper;

    @Mapping(target = "chatId", source = "chatId")
    @Mapping(target = "messageId", source = "messageId")
    @Mapping(target = "message" , source = "stockDto", qualifiedByName = "mapStockDtoAsString")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract MessageShortDto createDto(long chatId, int messageId, StockDto stockDto);


    @Named("mapStockDtoAsString")
    public String mapStockDtoAsString(StockDto stockDto) {
        try {
            return mapper.writeValueAsString(stockDto);
        } catch (JsonProcessingException ex) {
            throw new MapperException("can't convert stock:" + stockDto, ex);
        }
    }

}