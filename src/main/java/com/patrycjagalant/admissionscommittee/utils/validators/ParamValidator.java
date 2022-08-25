package com.patrycjagalant.admissionscommittee.utils.validators;


import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;
@UtilityClass
public class ParamValidator {

    public static boolean isIntegerOrLong(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static boolean isNameInvalid(String name) {
        Pattern pattern = Pattern.compile("^[ \\p{L}-]{2,150}$");
        if (name == null) {
            return true;
        }
        return !pattern.matcher(name).matches();
    }

}
