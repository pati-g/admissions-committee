package com.patrycjagalant.admissionscommittee.utils;

import com.patrycjagalant.admissionscommittee.utils.validators.FileValidator;
import com.patrycjagalant.admissionscommittee.utils.validators.ParamValidator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanValidatorsTests {
    @Test
    void shouldReturnFalse_WhenInvalidFile() {
        String name = "test-incorrect-file.txt";
        byte[] content = new byte[]{0,1,3,4};
        String name2 = "another-file.png";
        MultipartFile incorrectContentTypeFile = new MockMultipartFile(name, name, "text/plain", (content));
        MultipartFile emptyFile = new MockMultipartFile(name2, name2, "image/png", (byte[]) null);

        assertThat(FileValidator.validate(incorrectContentTypeFile)).isFalse();
        assertThat(FileValidator.validate(emptyFile)).isFalse();
    }

    @Test
    void shouldReturnTrue_WhenValidFile() {
        String name1 = "test-file.pdf";
        String name2 = "another-file.png";
        byte[] content = new byte[]{0,1,3,4};
        MultipartFile file1 = new MockMultipartFile(name1, name1, "application/pdf", content);
        MultipartFile file2 = new MockMultipartFile(name2, name2, "image/png", content);

        assertThat(FileValidator.validate(file1)).isTrue();
        assertThat(FileValidator.validate(file2)).isTrue();
    }

    @Test
    void shouldReturnTrue_WhenInputIsIntegerOrLong() {
        assertThat(ParamValidator.isIntegerOrLong("1392592760926809238515820598239358709")).isTrue();
        assertThat(ParamValidator.isIntegerOrLong("15")).isTrue();
        assertThat(ParamValidator.isIntegerOrLong("900000")).isTrue();
        assertThat(ParamValidator.isIntegerOrLong("-500")).isTrue();
    }

    @Test
    void shouldReturnFalse_WhenInputIsNotValidNumber() {
        assertThat(ParamValidator.isIntegerOrLong(null)).isFalse();
        assertThat(ParamValidator.isIntegerOrLong("15.8")).isFalse();
        assertThat(ParamValidator.isIntegerOrLong("trololo")).isFalse();
        assertThat(ParamValidator.isIntegerOrLong("500L")).isFalse();
    }

    @Test
    void shouldReturnTrue_WhenInputIsValidName() {
        assertThat(ParamValidator.isNameInvalid("Стефанія Сусанна-Лена")).isFalse();
        assertThat(ParamValidator.isNameInvalid("夏美 菜摘 なつみ")).isFalse();
        assertThat(ParamValidator.isNameInvalid("Anastasia-Maria Cella")).isFalse();
        assertThat(ParamValidator.isNameInvalid("Wolfeschlegelsteinhausenbergerdorff")).isFalse();
    }

    @Test
    void shouldReturnFalse_WhenInputNameIsInvalid() {
        assertThat(ParamValidator.isNameInvalid(null)).isTrue();
        assertThat(ParamValidator.isNameInvalid("bodenga@wp.pl")).isTrue();
        assertThat(ParamValidator.isNameInvalid("U2")).isTrue();
        assertThat(ParamValidator.isNameInvalid("U-<ME")).isTrue();
    }
}
