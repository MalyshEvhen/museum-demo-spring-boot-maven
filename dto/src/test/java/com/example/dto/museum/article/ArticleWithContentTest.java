package com.example.dto.museum.article;

import com.example.dto.config.AbstractDtoTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArticleWithContentTest extends AbstractDtoTest<ArticleWithContent> {
    
    @BeforeEach
    void setUp() {
        setField("tags", Set.of());
    }

    @AfterEach
    void tearDown() {
        clearFields();
    }
    
    @Test
    void shouldPass() {
        var articleWithContent = getModel();
        var violations = validate(articleWithContent);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void invalidTextFields(String value) {
        var fields = Stream.of("title", "content", "authorUsername").toList();
        fields.stream()
                .peek(field -> setField(field, value))
                .map(field -> getModel())
                .map(this::validate)
                .map(Set::isEmpty)
                .forEach(Assertions::assertFalse);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-100L, 0L})
    void invalidId(Long value) {
        var fields = Stream.of("id", "authorId").toList();
        fields.stream()
                .peek(field -> setField(field, value))
                .map(field -> getModel())
                .map(this::validate)
                .map(Set::isEmpty)
                .forEach(Assertions::assertFalse);
    }
}