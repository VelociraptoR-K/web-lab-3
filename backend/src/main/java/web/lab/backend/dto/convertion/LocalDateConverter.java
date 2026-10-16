package web.lab.backend.dto.convertion;

import jakarta.ws.rs.ext.ParamConverter;

import java.time.LocalDate;

public class LocalDateConverter implements ParamConverter<LocalDate> {

    @Override
    public LocalDate fromString(String value) {
        return LocalDate.parse(value);
    }

    @Override
    public String toString(LocalDate value) {
        return value == null ? null : value.toString();
    }
}
