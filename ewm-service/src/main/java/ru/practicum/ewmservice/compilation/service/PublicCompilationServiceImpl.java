package ru.practicum.ewmservice.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.compilation.DTO.CompilationDTO;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.compilation.repository.PublicCompilationRepository;
import ru.practicum.ewmservice.exception.IncorrectlyException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private PublicCompilationRepository publicCompilationRepository;
    private CompilationMapper compilationMapper;

    public PublicCompilationServiceImpl(PublicCompilationRepository publicCompilationRepository,
                                        CompilationMapper compilationMapper) {
        this.publicCompilationRepository = publicCompilationRepository;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public List<CompilationDTO> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations = publicCompilationRepository.findByPinned(pinned, PageRequest.of(from, size));
        return compilations.stream().map(compilationMapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public CompilationDTO findCompilation(Integer compId) {
        return compilationMapper.mapEntityToDTO(publicCompilationRepository.findById(compId).orElseThrow(() ->
                new IncorrectlyException(String.format("Failed: compilation. " +
                        "Error: подборка не найдена. Value:%s", compId))));
    }
}
