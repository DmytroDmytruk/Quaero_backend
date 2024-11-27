package org.startup.quaero.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.startup.quaero.database.entities.SelectionStage;

import java.util.List;

public interface SelectionStageRepo extends JpaRepository<SelectionStage, Long> {
    List<SelectionStage> findBySelectionProcessId(Long selectionProcessId);
}
