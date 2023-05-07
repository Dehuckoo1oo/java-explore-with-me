package ru.practicum.ewmservice.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin.DTO.UpdateEventAdminRequest;
import ru.practicum.ewmservice.admin.service.AdminCategoryService;
import ru.practicum.ewmservice.admin.service.AdminCompilationService;
import ru.practicum.ewmservice.admin.service.AdminEventService;
import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.category.DTO.NewCategoryDTO;
import ru.practicum.ewmservice.compilation.DTO.CompilationDTO;
import ru.practicum.ewmservice.compilation.DTO.NewCompilationDTO;
import ru.practicum.ewmservice.compilation.DTO.UpdateCompilationDTO;
import ru.practicum.ewmservice.event.DTO.EventFullDTO;
import ru.practicum.ewmservice.user.DTO.NewUserDTO;
import ru.practicum.ewmservice.user.DTO.UserDTO;
import ru.practicum.ewmservice.admin.service.AdminUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private AdminUserService adminUserService;
    private AdminCategoryService adminCategoryService;
    private AdminEventService adminEventService;
    private AdminCompilationService adminCompilationService;

    public AdminController(AdminUserService adminUserService, AdminCategoryService adminCategoryService,
                           AdminEventService adminEventService, AdminCompilationService adminCompilationService) {
        this.adminUserService = adminUserService;
        this.adminCategoryService = adminCategoryService;
        this.adminEventService = adminEventService;
        this.adminCompilationService = adminCompilationService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody NewUserDTO newUserDTO) {
        return adminUserService.createUser(newUserDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}")
    public UserDTO deleteUser(@PathVariable Integer userId) {
        return adminUserService.deleteUser(userId);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsers(@RequestParam(name = "ids", required = false) List<Integer> ids,
                                  @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return adminUserService.getUsers(ids, from, size);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDTO updateCategory(@PathVariable Integer catId, @Valid @RequestBody NewCategoryDTO newCategoryDTO) {
        return adminCategoryService.updateCategory(catId, newCategoryDTO);
    }

    @PostMapping("/categories")
    public CategoryDTO createCategory(@Valid @RequestBody NewCategoryDTO newCategoryDTO) {
        return adminCategoryService.createCategory(newCategoryDTO);
    }

    @DeleteMapping("/categories/{catId}")
    public CategoryDTO deleteCategory(@PathVariable Integer catId) {
        return adminCategoryService.deleteCategory(catId);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDTO> getEvents(@RequestParam(name = "users", required = false) List<Long> ids,
                                        @RequestParam(name = "states", required = false) List<String> states,
                                        @RequestParam(name = "categories", required = false) List<Integer> categories,
                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                        @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                        @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return adminEventService.getEvents(ids, states, categories, rangeStart, rangeEnd, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/categories/{eventId}")
    public EventFullDTO updateEvent(@PathVariable Integer eventId,
                                    @RequestBody UpdateEventAdminRequest updateEventDTO) {
        return adminEventService.updateEvent(eventId, updateEventDTO);
    }

    @PostMapping("/compilations")
    public CompilationDTO createCompilation(@RequestBody NewCompilationDTO newCompilationDTO) {
        return adminCompilationService.createCompilation(newCompilationDTO);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDTO updateCompilation(@RequestBody UpdateCompilationDTO updateCompilationDTO,
                                            @PathVariable Integer compId) {
        return adminCompilationService.updateCompilation(updateCompilationDTO, compId);
    }

    @DeleteMapping("/compilations/{compId}")
    public CompilationDTO deleteCompilation(@PathVariable Integer compId) {
        return adminCompilationService.deleteCompilation(compId);
    }

}
