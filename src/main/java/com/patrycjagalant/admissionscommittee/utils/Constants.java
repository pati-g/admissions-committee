package com.patrycjagalant.admissionscommittee.utils;

public class Constants {
    private Constants(){}

    // Template paths
    public static final String REGISTER = "register";
    public static final String VIEW_PROFILE = "applicants/viewProfile";
    public static final String FACULTIES = "faculties/allFaculties";
    public static final String ALL_APPLICANTS = "applicants/allApplicants";
    public static final String APPLICANTS_EDIT_PROFILE = "applicants/editProfile";

    // Redirects
    public static final String REDIRECT_HOME = "redirect:/";
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
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
}