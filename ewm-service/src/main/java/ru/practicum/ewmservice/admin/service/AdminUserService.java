package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.user.DTO.NewUserDTO;
import ru.practicum.ewmservice.user.DTO.UserDTO;

import java.util.List;

public interface AdminUserService {
    public UserDTO createUser(NewUserDTO newUserDTO);

    public UserDTO deleteUser(Integer userId);

    public List<UserDTO> getUsers(List<Integer> ids, int from, int size);
}
