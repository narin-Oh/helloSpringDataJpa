package kr.ac.hansung.cse.hellospringdatajpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName(); // 로그인한 사용자 이메일
            model.addAttribute("message", "환영합니다, " + email + "님!");
        } else {
            model.addAttribute("message", "홈페이지에 오신 것을 환영합니다.");
        }
        return "home";  // home.html 뷰
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // templates/login.html
    }

}
