package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.service.EnrollmentRequestService;
import org.springframework.stereotype.Controller;

@Controller
//@RequestMapping("/request")
public class EnrollmentRequestController {
    private final EnrollmentRequestService requestService;

    public EnrollmentRequestController(EnrollmentRequestService requestService) {
        this.requestService = requestService;
    }
    // Show all for logged in applicant: return list of application requests for applicant who is logged in
//    @GetMapping("/myrequests")
//    public String getUserRequests(Model model, @AuthenticationPrincipal User user) {
//        UserMapper mapper = new UserMapper();
//        UserDto userDto = mapper.mapToDTO(user);
//        model.addAttribute("userDTO", userDto);
//
//        Set<EnrollmentRequest> requestDtos =
//        if (requestDtos != null) {
//            model.addAttribute("requestDtos", requestDtos);
//        }
//
//        return "applicants/editProfile";
//    }

    // Create new: ApplicantDto clicks the 'apply' button next to chosen faculty, gets signed up, enrollmentRequest gets created


    // Show all: for admin only

    // Delete specific request (unsign)
}