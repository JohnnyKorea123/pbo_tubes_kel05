package com.registrasi.mahasiswa.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.service.CalonMahasiswaService;
import com.registrasi.mahasiswa.service.JurusanService;
import com.registrasi.mahasiswa.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/calonMahasiswa")
public class CalonMahasiswaController {
    @Autowired
    private CalonMahasiswaService calonMahasiswaService;

    @Autowired
    private JurusanService jurusanService;

     @Autowired private 
    UserService userService;


    @GetMapping("/inputBiodata")
    public String inputBiodataForm(Model model) {
        model.addAttribute("calonMahasiswa", new CalonMahasiswaDTO());
        model.addAttribute("jurusanList", jurusanService.getAllJurusan());
        return "calonMahasiswa/inputBiodata";
    }

    @PostMapping("/inputBiodata")
    public String saveBiodata(@ModelAttribute CalonMahasiswaDTO calonMahasiswaDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        CalonMahasiswa existingCalonMahasiswa = calonMahasiswaService.findByUser(user);
        if (existingCalonMahasiswa != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Sudah Mengisi Biodata");
            return "redirect:/calonMahasiswa/dashboard";
        }
        CalonMahasiswaDTO savedCalonMahasiswa = calonMahasiswaService.saveCalonMahasiswa(calonMahasiswaDTO, user);
        session.setAttribute("calonMahasiswa", savedCalonMahasiswa);
        redirectAttributes.addFlashAttribute("successMessage", "Sudah Mengisi Biodata");
        return "redirect:/calonMahasiswa/dashboard";
    }

    @GetMapping("/hasilTest")
    public String lihatHasilTest(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        CalonMahasiswa calonMahasiswa = calonMahasiswaService.findByUser(user);
        if (calonMahasiswa == null) {
            model.addAttribute("errorMessage", "Mohon Masukan Biodata Terlebih Dahulu dan Memilih Jurusan");
            return "calonMahasiswa/inputBiodata";
        }
        model.addAttribute("calonMahasiswa", calonMahasiswa);
        System.out.println("Calon Mahasiswa: " + calonMahasiswa); // Tambahkan log ini
        return "calonMahasiswa/lihatHasilTest";
    }

    @GetMapping("/profile")
    public String mahasiswaProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equals(user.getRole())) {
            return "redirect:/login";
        }
        CalonMahasiswa calonMahasiswa = calonMahasiswaService.findByUser(user);
        if (calonMahasiswa == null) {
            model.addAttribute("errorMessage", "Data calon mahasiswa tidak ditemukan.");
            return "error";
        }
        model.addAttribute("calonMahasiswa", calonMahasiswa);
        model.addAttribute("user", user); // Tambahkan objek User ke model
        return "calonMahasiswa/profile";
    }
    
    @PostMapping("/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        try {
            userService.saveProfilePicture(user, file);
            redirectAttributes.addFlashAttribute("message", "Gambar profil berhasil diunggah!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal mengunggah gambar profil.");
        }
        return "redirect:/calonMahasiswa/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        model.addAttribute("message", "Anda telah berhasil logout.");
        return "calonMahasiswa/logout";
    }
}

