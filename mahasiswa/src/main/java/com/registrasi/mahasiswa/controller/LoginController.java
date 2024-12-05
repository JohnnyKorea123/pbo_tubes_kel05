package com.registrasi.mahasiswa.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.registrasi.mahasiswa.dto.UserDTO;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.repository.UserRepository;
import com.registrasi.mahasiswa.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session, Model model) {
        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user != null && BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            session.setAttribute("user", user);
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else if ("USER".equals(user.getRole())) {
                return "redirect:/calonMahasiswa/dashboard";
            }
        }
        model.addAttribute("message", "Username Atau Password salah");
        return "login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user); 
        return "admin/dashboard";
    }

    @GetMapping("/calonMahasiswa/dashboard")
    public String calonMahasiswaDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "calonMahasiswa/dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

   @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDTO userDTO, BindingResult result, Model model) {
    if (result.hasErrors()) {
        return "register";
    }
    if (userRepository.findByUsername(userDTO.getUsername()) != null) {
        model.addAttribute("message", "Username telah terdaftar");
        return "register";
    }
    userService.saveUser(userDTO);
    model.addAttribute("message", "Akun berhasil didaftarkan!");
    return "registerSuccess";
}


}
