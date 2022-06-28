package com.systex.msg.practice.domain.share;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.systex.msg.practice.domain.train.aggregate.vo.TrainKind;

@Converter(autoApply = true)
public class TrainKindConverter implements AttributeConverter<TrainKind, String> {
 
    @Override
    public String convertToDatabaseColumn(TrainKind source) {
        if (source == null) {
            return null;
        }
        return source.getCode();
    }

    @Override
    public TrainKind convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(TrainKind.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}