package org.startup.quaero.dto.selectionprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SelectionStageDto {
    private Long id;
    private String name;
    private String description;
    private Integer order;
    private Long assignedUserId;
    private Boolean completed;
}
