package ru.practicum.ewmservice.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.compilation.DTO.CompilationDTO;
import ru.practicum.ewmservice.compilation.DTO.NewCompilationDTO;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    private final EventMapper eventMapper;

    public CompilationMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public Compilation mapDTOToEntity(CompilationDTO compilationDTO, Set<Event> events) {
        Compilation compilation = Compilation.builder()
                .title(compilationDTO.getTitle())
                .pinned(compilationDTO.getPinned())
                .events(events)
                .build();
        return compilation;
    }

    public CompilationDTO mapEntityToDTO(Compilation compilation) {
        CompilationDTO compilationDTO = CompilationDTO.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(eventMapper::mapEntityToShortDTO)
                        .collect(Collectors.toSet()))
                .build();
        return compilationDTO;
    }

    public Compilation mapNewDTOToEntity(NewCompilationDTO newCompilationDTO, List<Event> events) {
        Set<Event> eventSet = new HashSet<>(events);
        Compilation compilation = Compilation.builder()
                .events(eventSet)
                .pinned(newCompilationDTO.getPinned())
                .title(newCompilationDTO.getTitle())
                .build();
        return compilation;
    }
}
