package org.tutorBridge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tutorBridge.dto.TutorDTO;
import org.tutorBridge.entities.Tutor;

@Mapper(componentModel = "spring")
public interface TutorMapper {
    Tutor toEntity(TutorDTO tutorDTO);

    TutorDTO toDto(Tutor tutor);
}
