package ru.practicum.ewmservice.user.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDTO {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
}
