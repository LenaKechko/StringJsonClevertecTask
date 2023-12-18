package ru.clevertec.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"{\"id\" :\"123\", \"name\" : \"Name speciality\"}",
            "{[\"id\" :\"123\", \"name\" : \"Name speciality\"]}",
            "{() : [\"id\" :\"123\", \"name\" : \"Name speciality\"]}"})
    void isValidBracketsShouldReturnTrue(String json) {
        //given

        // when-then
        assertTrue(Validator.isValidBrackets(json));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"id\" :\"123\", \"name\" : \"Name speciality\"}",
            "{\"id\" :\"123\", \"name\" : \"Name speciality\"",
            "{[\"id\" :\"123\", \"name\" : \"Name speciality\"",
            "{[\"id\" :\"123\", \"name\" : \"Name speciality\"}]",
            "{( : \"id\" :\"123\", \"name\" : \"Name speciality\"]}",
            "{( : [\"id\" :\"123\", \"name\" : \"Name speciality\")}]"})
    void isValidBracketsShouldReturnFalse(String json) {
        //given

        // when-then
        assertFalse(Validator.isValidBrackets(json));
    }

    @Test
    void isValidQuoteShouldReturnTrue() {
        //given
        String jsonString = "{\"id\" :\"123\", \"name\" : \"Name speciality\"}";

        // when-then
        assertTrue(Validator.isValidQuote(jsonString));
    }

    @Test
    void isValidQuoteShouldReturnFalse() {
        //given
        String jsonString = "{id\" :\"123\", \"name\" : \"Name speciality\"}";

        // when-then
        assertFalse(Validator.isValidQuote(jsonString));
    }

    @Test
    void isValidColonShouldReturnTrue() {
        //given
        String jsonString = "{\"id\" :\"123\", \"name\" : \"Name speciality\"}";

        // when-then
        assertTrue(Validator.isValidColon(jsonString));
    }

    @Test
    void isValidColonShouldReturnFalse() {
        //given
        String jsonString = "{\"id\"}";

        // when-then
        assertFalse(Validator.isValidColon(jsonString));
    }
}