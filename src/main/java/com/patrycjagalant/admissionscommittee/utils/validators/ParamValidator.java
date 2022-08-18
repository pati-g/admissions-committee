package com.patrycjagalant.admissionscommittee.utils.validators;


import java.util.regex.Pattern;

public class ParamValidator {
    private ParamValidator() {
    }

    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[ \\p{L}\\d-]{2,150}$");
        if (name == null) {
            return false;
        }
        return pattern.matcher(name).matches();
    }

}
