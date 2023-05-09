package ru.practicum.ewmservice.compilation.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.DTO.CompilationDTO;
import ru.practicum.ewmservice.compilation.service.PublicCompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    @GetMapping
    public List<CompilationDTO> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                @RequestParam(name = "from", required = false, defaultValue = "0")
                                                int from,
                                                @RequestParam(name = "size", required = false, defaultValue = "10")
                                                int size) {
        return publicCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDTO getCompilationById(@PathVariable Integer compId) {
        return publicCompilationService.findCompilation(compId);
    }
}
