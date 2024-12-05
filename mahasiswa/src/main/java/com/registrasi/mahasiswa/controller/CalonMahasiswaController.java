package com.registrasi.mahasiswa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.service.CalonMahasiswaService;
import com.registrasi.mahasiswa.service.JurusanService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/calonMahasiswa")
public class CalonMahasiswaController {
    @Autowired
    private CalonMahasiswaService calonMahasiswaService;

    @Autowired
    private JurusanService jurusanService;


    // input biodata start
    @GetMapping("/inputBiodata")
    public String inputBiodataForm(Model model) {
        model.addAttribute("calonMahasiswa", new CalonMahasiswaDTO());
        model.addAttribute("jurusanList", jurusanService.getAllJurusan());
        return "calonMahasiswa/inputBiodata";
    }

    @PostMapping("/inputBiodata")
    public String saveBiodata(@ModelAttribute CalonMahasiswaDTO calonMahasiswaDTO, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        CalonMahasiswaDTO savedCalonMahasiswa = calonMahasiswaService.saveCalonMahasiswa(calonMahasiswaDTO, user);
        session.setAttribute("calonMahasiswa", savedCalonMahasiswa);
        return "redirect:/calonMahasiswa/dashboard";
    }

    // input biodata ends
    
    @GetMapping("/hasilTest")
    public String lihatHasilTest(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        CalonMahasiswa calonMahasiswa = calonMahasiswaService.findByUser(user);
        if (calonMahasiswa == null) {
            return "redirect:/calonMahasiswa/inputBiodata";
        }
        model.addAttribute("calonMahasiswa", calonMahasiswa);
        System.out.println("Calon Mahasiswa: " + calonMahasiswa); // Tambahkan log ini
        return "calonMahasiswa/lihatHasilTest";
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
