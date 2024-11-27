package org.startup.quaero.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.startup.quaero.database.entities.SelectionProcess;
import org.startup.quaero.dto.selectionprocess.SelectionProcessDto;
import org.startup.quaero.dto.selectionprocess.SelectionStageDto;

@Component
public class SelectionProcessToSelectionProcessDtoConverter implements Converter<SelectionProcess, SelectionProcessDto> {

    @Override
    public SelectionProcessDto convert(MappingContext<SelectionProcess, SelectionProcessDto> context) {
        SelectionProcess source = context.getSource();
        if (source == null) {
            return null;
        }

        // Мапимо основні поля SelectionProcess
        SelectionProcessDto.SelectionProcessDtoBuilder dtoBuilder = SelectionProcessDto.builder()
                .id(source.getId())
                .name(source.getTitle());

        // Мапимо список SelectionStage у SelectionStageDto
        if (source.getStages() != null) {
            dtoBuilder.stages(
                    source.getStages().stream()
                            .map(stage -> SelectionStageDto.builder()
                                    .id(stage.getId())
                                    .name(stage.getName())
                                    .description(stage.getDescription())
                                    .assignedUserId(stage.getAssignedTo().getId())
                                    .order(stage.getOrder())
                                    .completed(stage.getCompleted())
                                    .build())
                            .toList()
            );
        }

        return dtoBuilder.build();
    }
}