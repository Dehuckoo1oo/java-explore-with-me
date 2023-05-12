package ru.practicum.ewmservice.comment.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentDTO {
    @NotBlank
    private String text;
}
