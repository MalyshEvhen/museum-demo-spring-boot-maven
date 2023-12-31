package com.example.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.instancio.Select.field;

@ExtendWith(InstancioExtension.class)
public abstract class AbstractInstancioTest<T> {
    private final Map<String, Object> additionalSettings = new HashMap<>();
    private Validator validator;

    @BeforeEach
    void init() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @WithSettings
    private static final Settings settings = Settings.create()
            .set(Keys.BEAN_VALIDATION_ENABLED, true)
            .lock();

    protected T getModel() {
        return additionalSettings.isEmpty() ? createDefaultModel() : createModifiedModel();
    }

    private T createModifiedModel() {
        Class<T> clazz = extractClass();
        var fieldValueMap = getAdditionalSettings();
        var classApi = Instancio.of(clazz).withSettings(settings);
        for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            classApi = classApi.set(field(fieldName), value);
        }
        return classApi.create();
    }

    private T createDefaultModel() {
        Class<T> clazz = extractClass();
        return Instancio.of(clazz)
                .withSettings(settings)
                .create();
    }

    @SuppressWarnings("unchecked")
    private Class<T> extractClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }

    protected Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }

    protected Map<String, Object> getAdditionalSettings() {
        return additionalSettings;
    }

    protected void set(String field, Object value) {
        additionalSettings.put(field, value);
    }

    protected void clearAdditionalSettings() {
        additionalSettings.clear();
    }
}
