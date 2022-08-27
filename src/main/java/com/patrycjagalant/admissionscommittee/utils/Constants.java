package com.patrycjagalant.admissionscommittee.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    // Template paths
    public static final String EDIT_ACCOUNT = "editAccount";
    public static final String ALL_REQUESTS = "requests/allRequests";
    public static final String REGISTER = "register";
    public static final String VIEW_PROFILE = "applicants/viewProfile";
    public static final String FACULTIES = "faculties/allFaculties";
    public static final String ALL_APPLICANTS = "applicants/allApplicants";
    public static final String APPLICANTS_EDIT_PROFILE = "applicants/editProfile";

    // Redirects
    public static final String REDIRECT_ALL_REQUESTS = "redirect:/request/all";
    public static final String REDIRECT_HOME = "redirect:/";
    public static final String REDIRECT_EDIT_ACCOUNT = "redirect:/user/{username}/edit";
    public static final String REDIRECT_REGISTER = "redirect:/register";
    public static final String REDIRECT_NEW_FACULTY = "redirect:/faculties/new";
    public static final String REDIRECT_FACULTIES = "redirect:/faculties";
    public static final String REDIRECT_APPLICANT_ALL = "redirect:/applicant/all";
    public static final String REDIRECT_EDIT_APPLICANT = "redirect:/applicant/{username}/edit";
    // Attribute names
    public static final String APPLICANT_DTO = "applicantDTO";
    public static final String USER_DTO = "userDTO";
    public static final String FACULTY_DTO = "facultyDTO";
    public static final String SCORES = "scores";
    public static final String REQUESTS = "requests";
    public static final String ERROR = "errormsg";
    public static final String MESSAGE = "message";

    // Other
    public static final String EMAIL_PATTERN =
            "^(?=.{1,64}@)[\\p{L}\\d_-]+(\\.[\\p{L}\\d_-]++)*+@[^-][\\p{L}\\d-]+(\\.[\\p{L}\\d-]++)*(\\.\\p{L}{2,})$";
    public static final String COULD_NOT_FIND_USER_WITH_USERNAME = "Could not find user with username: ";
    public static final String COULD_NOT_FIND_USER_WITH_ID = "Could not find user with ID: ";
    public static final String ENTER_USERNAME_TO_LOG_IN = "Please enter username to log in";
    public static final String COULD_NOT_FIND_SUBJECT = "Could not find subject with ID: ";

    public static final String INCORRECT_REQUEST = "Incorrect request ID, please try again";
    public static final String INCORRECT_REQUEST_ID = "Incorrect request ID: ";
}