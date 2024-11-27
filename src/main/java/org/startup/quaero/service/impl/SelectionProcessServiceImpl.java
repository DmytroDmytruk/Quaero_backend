package org.startup.quaero.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.startup.quaero.database.entities.SelectionProcess;
import org.startup.quaero.database.entities.SelectionStage;
import org.startup.quaero.database.repos.SelectionProcessRepo;
import org.startup.quaero.database.repos.SelectionStageRepo;
import org.startup.quaero.dto.selectionprocess.SelectionProcessDto;
import org.startup.quaero.dto.selectionprocess.SelectionStageDto;
import org.startup.quaero.service.SelectionProcessService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectionProcessServiceImpl implements SelectionProcessService {
//
//    @Autowired
//    private SelectionProcessRepo selectionProcessRepo;
//
//    @Autowired
//    private SelectionStageRepo selectionStageRepo;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    public SelectionProcessDto getSelectionProcessWithStages(Long selectionProcessId) {
//        SelectionProcess selectionProcess = selectionProcessRepo.findById(selectionProcessId)
//                .orElseThrow(() -> new IllegalArgumentException("Selection process not found"));
//
//        List<SelectionStage> stages = selectionStageRepo.findBySelectionProcessId(selectionProcessId);
//        SelectionProcessDto selectionProcessDto = modelMapper.map(selectionProcess, SelectionProcessDto.class);
//
//        List<SelectionStageDto> stageDtos = stages.stream()
//                .map(stage -> {
//                    SelectionStageDto stageDto = modelMapper.map(stage, SelectionStageDto.class);
//                    stageDto.setAssignedTo(stage.getAssignedTo().getFirstName() + " " + stage.getAssignedTo().getLastName());
//                    return stageDto;
//                })
//                .collect(Collectors.toList());
//        selectionProcessDto.setStages(stageDtos);
//        return selectionProcessDto;
//    }
}
