package ru.practicum.ewmservice.admin.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.repository.AdminCompilationRepository;
import ru.practicum.ewmservice.admin.repository.AdminEventRepository;
import ru.practicum.ewmservice.compilation.DTO.CompilationDTO;
import ru.practicum.ewmservice.compilation.DTO.NewCompilationDTO;
import ru.practicum.ewmservice.compilation.DTO.UpdateCompilationDTO;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.exception.IncorrectlyException;

import java.util.HashSet;
import java.util.List;

@Service
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private AdminCompilationRepository adminCompilationRepository;
    private AdminEventRepository adminEventRepository;
    private EventMapper eventMapper;
    private CompilationMapper compilationMapper;

    public AdminCompilationServiceImpl(AdminCompilationRepository adminCompilationRepository, EventMapper eventMapper,
                                       AdminEventRepository adminEventRepository, CompilationMapper compilationMapper) {
        this.adminCompilationRepository = adminCompilationRepository;
        this.adminEventRepository = adminEventRepository;
        this.eventMapper = eventMapper;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public CompilationDTO createCompilation(NewCompilationDTO newCompilationDTO) {
        List<Event> events = adminEventRepository.findAllById(newCompilationDTO.getEvents());
        /*List<EventShortDTO> eventShortDTOs = events.stream().map(eventMapper::mapEntityToShortDTO)
                .collect(Collectors.toList());*/
        Compilation compilation = compilationMapper.mapNewDTOToEntity(newCompilationDTO, events);
        Compilation savedCompilation = adminCompilationRepository.save(compilation);
        CompilationDTO compilationDTO = compilationMapper.mapEntityToDTO(savedCompilation);
        return compilationDTO;
    }

    @Override
    public CompilationDTO updateCompilation(UpdateCompilationDTO updateCompilationDTO, Integer compId) {
        Compilation compilation = findCompilation(compId);
        List<Event> events = adminEventRepository.findAllById(updateCompilationDTO.getEvents());

        if (updateCompilationDTO.getTitle() != null) {
            compilation.setTitle(updateCompilationDTO.getTitle());
        }

        if (updateCompilationDTO.getPinned() != null) {
            compilation.setPinned(updateCompilationDTO.getPinned());
        }
        compilation.setEvents(new HashSet<>(events));
        return compilationMapper.mapEntityToDTO(adminCompilationRepository.save(compilation));
    }

    @Override
    public CompilationDTO deleteCompilation(Integer compId) {
        Compilation compilation = findCompilation(compId);
        adminCompilationRepository.delete(compilation);
        return compilationMapper.mapEntityToDTO(compilation);
    }

    private Compilation findCompilation(Integer compId) {
        return adminCompilationRepository.findById(compId).orElseThrow(() ->
                new IncorrectlyException(String.format("Failed: compilation. " +
                        "Error: подборка не найдена. Value:%s", compId)));
    }

}
