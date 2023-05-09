package ru.practicum.ewmservice.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.compilation.model.Compilation;

@Repository
public interface AdminCompilationRepository extends JpaRepository<Compilation, Integer> {
}
