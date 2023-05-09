package ru.practicum.ewmservice.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.compilation.model.Compilation;

import java.util.List;

@Repository
public interface PublicCompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query("SELECT c FROM Compilation c WHERE (:pinned IS NULL OR c.pinned = :pinned)")
    List<Compilation> findByPinned(@Param("pinned") Boolean pinned, Pageable pageable);

}
