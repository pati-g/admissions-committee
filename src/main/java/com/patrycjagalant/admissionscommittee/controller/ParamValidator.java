package com.patrycjagalant.admissionscommittee.controller;


import java.util.regex.Pattern;
public class ParamValidator {
    private ParamValidator(){}
    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
