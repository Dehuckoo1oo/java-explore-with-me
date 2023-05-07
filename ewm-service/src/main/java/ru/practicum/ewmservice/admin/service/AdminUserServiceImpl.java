package ru.practicum.ewmservice.admin.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.DTO.NewUserDTO;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.DTO.UserDTO;
import ru.practicum.ewmservice.admin.repository.AdminUserRepository;
import ru.practicum.ewmservice.exception.NotFoundResourceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    private AdminUserRepository adminUserRepository;
    private UserMapper userMapper;

    public AdminUserServiceImpl(AdminUserRepository adminUserRepository, UserMapper userMapper) {
        this.adminUserRepository = adminUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createUser(NewUserDTO newUserDTO) {
        User user = adminUserRepository.save(userMapper.mapUserFromNewDTO(newUserDTO));
        return userMapper.mapDTOFromUser(user);
    }

    @Override
    public UserDTO deleteUser(Integer userId) {
        User user = getUser(userId);
        adminUserRepository.delete(user);
        return userMapper.mapDTOFromUser(user);
    }

    @Override
    public List<UserDTO> getUsers(List<Integer> ids, int from, int size) {
        int page = 0;
        if (from != 0) {
            page = from / size;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<UserDTO> userDTOs = new ArrayList<>();
        adminUserRepository.findAllByIdIn(ids, pageable).forEach(usr -> userDTOs.add(userMapper.mapDTOFromUser(usr)));
        return userDTOs;
    }

    private User getUser(Integer userId) {
        Optional<User> user = adminUserRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundResourceException(String.format("User with id=%s was not found", userId));
        }
    }
}
