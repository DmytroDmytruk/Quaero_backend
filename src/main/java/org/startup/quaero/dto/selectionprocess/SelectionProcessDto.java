package org.startup.quaero.dto.selectionprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SelectionProcessDto {
    private Long id;
    private String name;
    private String assignedTo;
    private List<SelectionStageDto> stages;
}
