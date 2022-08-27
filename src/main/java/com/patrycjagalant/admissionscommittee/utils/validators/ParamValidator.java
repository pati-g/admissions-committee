package com.patrycjagalant.admissionscommittee.utils.validators;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * The {@code ParamValidator} class is a utility class for validating String input.
 * It provides two methods:
 * <ul>
 *     <li>{@link #isIntegerOrLong(String)} checks if the String provided can be parsed as
 *     a variable of type {@link Long}</li>
 *     <li>{@link #isNameInvalid(String)} matches the String to a regex pattern
 *      for first and last names of people from all nationalities.</li>
 * </ul>
 *
 * @author Patrycja Galant
 */

@UtilityClass
public class ParamValidator {

    /**
     * @return true if the validated String can be parsed as a {@link Long} variable
     */
    public static boolean isIntegerOrLong(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    /**
     * @return true if the String is a valid first or last name (is between 2-150
     * characters long, consists only of letters (upper- or lowercase, all Unicode letters),
     * spaces and dashes)
     */
    public static boolean isNameInvalid(String name) {
        Pattern pattern = Pattern.compile("^[ \\p{L}-]{2,150}$");
        if (name == null) {
            return true;
        }
        return !pattern.matcher(name).matches();
    }

}
