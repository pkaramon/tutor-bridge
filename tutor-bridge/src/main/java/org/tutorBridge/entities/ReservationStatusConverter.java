package org.tutorBridge.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, Character> {

    @Override
    public Character convertToDatabaseColumn(ReservationStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return null;
        }
        return ReservationStatus.fromCode(dbData);
    }
}
