package com.dmdev.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalDateFormatterTest {

    private static final String INCORRECT_DATE = "18.09.2022";
    private static final String CORRECT_DATE = "2022-09-18";

    @Test
    void testStringFormattingForDate() {
        LocalDate format = LocalDateFormatter.format(CORRECT_DATE);
        assertAll(
                () -> assertThat(format).isNotNull(),
                () -> assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(INCORRECT_DATE))
        );
    }

    @Test
    void testDateValidity() {
        Assertions.assertAll(
                () -> assertThat(LocalDateFormatter.isValid(CORRECT_DATE)).isTrue(),
                () -> assertThat(LocalDateFormatter.isValid(INCORRECT_DATE)).isFalse()
        );
    }


}
