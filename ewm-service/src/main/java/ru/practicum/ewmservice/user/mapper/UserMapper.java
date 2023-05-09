package ru.practicum.ewmservice.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.user.DTO.NewUserDTO;
import ru.practicum.ewmservice.user.DTO.UserShortDTO;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.DTO.UserDTO;

@Component
public class UserMapper {

    public User mapUserFromDTO(UserDTO userDTO) {
        User user = User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .build();
        return user;
    }

    public User mapUserFromNewDTO(NewUserDTO newUserDTO) {
        User user = User.builder()
                .name(newUserDTO.getName())
                .email(newUserDTO.getEmail())
                .build();
        return user;
    }

    public UserDTO mapDTOFromUser(User user) {
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return userDTO;
    }

    public UserShortDTO mapShortDTOFromUser(User user) {
        UserShortDTO userShortDTO = UserShortDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
        return userShortDTO;
    }


}
