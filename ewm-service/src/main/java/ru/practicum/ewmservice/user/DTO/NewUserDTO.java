package ru.practicum.ewmservice.user.DTO;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
}
