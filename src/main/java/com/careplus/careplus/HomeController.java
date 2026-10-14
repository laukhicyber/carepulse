
package com.careplus.careplus;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("loggedIn",
                Boolean.TRUE.equals(session.getAttribute("loggedIn")));
        model.addAttribute("phone",
                session.getAttribute("loggedInPhone"));
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/login/send-otp")
    public String sendOtp(
            @RequestParam("phone") String phone,
            Model model,
            HttpSession session) {

        if (!phone.matches("[6-9][0-9]{9}")) {
            model.addAttribute("message", "Invalid mobile number");
            return "login";
        }

        session.setAttribute("demoOtp", "123456");
        session.setAttribute("otpPhone", phone);

        model.addAttribute("phone", phone);
        model.addAttribute("demoOtp", "123456");
        model.addAttribute("message", "Demo OTP: 123456");

        return "otp";
    }

    @PostMapping("/login/verify-otp")
    public String verifyOtp(
            @RequestParam("phone") String phone,
            @RequestParam("otp") String otp,
            Model model,
            HttpSession session) {

        String savedOtp = (String) session.getAttribute("demoOtp");
        String savedPhone = (String) session.getAttribute("otpPhone");

        if (savedOtp != null
                && savedOtp.equals(otp)
                && phone.equals(savedPhone)) {

            session.setAttribute("loggedIn", true);
            session.setAttribute("loggedInPhone", phone);
            session.removeAttribute("demoOtp");
            session.removeAttribute("otpPhone");

            return "redirect:/";
        }

        model.addAttribute("phone", phone);
        model.addAttribute("demoOtp", "123456");
        model.addAttribute("message", "Wrong OTP. Try again.");

        return "otp";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        model.addAttribute("phone",
                session.getAttribute("loggedInPhone"));
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // Open appointment form
    @GetMapping("/appointments")
    public String appointments(HttpSession session, Model model) {
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        model.addAttribute("phone",
                session.getAttribute("loggedInPhone"));
        return "appointments";
    }

    // Submit appointment form
    @PostMapping("/appointments/book")
    public String bookAppointment(
            @RequestParam("patientName") String patientName,
            @RequestParam("phone") String phone,
            @RequestParam("doctor") String doctor,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            HttpSession session,
            Model model) {

        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        if (patientName.trim().isEmpty()
                || !phone.matches("[6-9][0-9]{9}")
                || doctor.trim().isEmpty()
                || date.trim().isEmpty()
                || time.trim().isEmpty()) {

            model.addAttribute("message",
                    "Please enter valid details.");
            model.addAttribute("phone",
                    session.getAttribute("loggedInPhone"));
            return "appointments";
        }

        model.addAttribute("patientName", patientName);
        model.addAttribute("phone", phone);
        model.addAttribute("doctor", doctor);
        model.addAttribute("date", date);
        model.addAttribute("time", time);

        return "confirmation";
    }
    @GetMapping("/doctors")
public String doctors() {
    return "doctors";
}

@PostMapping("/register")
public String registerUser(
        @RequestParam("name") String name,
        @RequestParam("phone") String phone,
        Model model) {

    if (name.trim().isEmpty()
            || !phone.matches("[6-9][0-9]{9}")) {
        model.addAttribute("message",
                "Please enter valid details.");
        return "register";
    }

    model.addAttribute("message",
            "Registration successful! Please login.");

    return "register";
}
}
