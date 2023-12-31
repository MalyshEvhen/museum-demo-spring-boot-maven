package com.example.dto.museum.author;

import com.example.domain.museum.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * DTO for {@link Author}
 */
@Validated
public record AuthorShortResponse(
        @NotNull @Positive Long id,
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String userFirstName,
        @NotNull @NotBlank String userLastName
) implements Serializable {
}