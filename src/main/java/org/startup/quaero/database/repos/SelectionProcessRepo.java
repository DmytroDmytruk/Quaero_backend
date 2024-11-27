package org.startup.quaero.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.startup.quaero.database.entities.SelectionProcess;

public interface SelectionProcessRepo extends JpaRepository<SelectionProcess, Long> {
}
