package org.startup.quaero.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.startup.quaero.database.entities.SelectionProcess;
import org.startup.quaero.database.entities.SelectionStage;
import org.startup.quaero.dto.selectionprocess.SelectionProcessDto;
import org.startup.quaero.dto.selectionprocess.SelectionStageDto;

public class SelectionStageToSelectionStageDtoConverter implements Converter<SelectionStage, SelectionStageDto> {
    @Override
    public SelectionStageDto convert(MappingContext<SelectionStage, SelectionStageDto> mappingContext) {
        return null;
    }
}
