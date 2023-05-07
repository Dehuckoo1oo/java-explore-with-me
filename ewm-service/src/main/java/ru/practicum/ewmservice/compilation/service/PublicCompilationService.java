package ru.practicum.ewmservice.compilation.service;

import ru.practicum.ewmservice.compilation.DTO.CompilationDTO;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDTO> getCompilations(Boolean pinned, int from, int size);
    CompilationDTO findCompilation(Integer compId);
}
