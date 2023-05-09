package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.compilation.DTO.CompilationDTO;
import ru.practicum.ewmservice.compilation.DTO.NewCompilationDTO;
import ru.practicum.ewmservice.compilation.DTO.UpdateCompilationDTO;


public interface AdminCompilationService {
    CompilationDTO createCompilation(NewCompilationDTO newCompilationDTO);

    CompilationDTO updateCompilation(UpdateCompilationDTO updateCompilationDTO, Integer compId);

    CompilationDTO deleteCompilation(Integer compId);
}
