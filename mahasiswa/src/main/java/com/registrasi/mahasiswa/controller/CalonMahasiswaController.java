package com.registrasi.mahasiswa.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.dto.JurusanDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.HasilTes;
import com.registrasi.mahasiswa.model.Jurusan;
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

    @Autowired
    private UserService userService;

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

    CalonMahasiswaDTO savedCalonMahasiswaDTO = calonMahasiswaService.saveCalonMahasiswa(calonMahasiswaDTO, user);
    Jurusan jurusanYangDiterima = calonMahasiswaService.tentukanJurusanYangDiterima(savedCalonMahasiswaDTO);
    savedCalonMahasiswaDTO.setStatusPenerimaan(jurusanYangDiterima != null ? "Diterima" : "Tidak Diterima");
    calonMahasiswaService.saveCalonMahasiswa(savedCalonMahasiswaDTO, user);

    session.setAttribute("calonMahasiswa", savedCalonMahasiswaDTO);
    redirectAttributes.addFlashAttribute("successMessage", "Sudah Mengisi Biodata");
    return "redirect:/calonMahasiswa/dashboard";
}


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(List.class, "jurusanYangDiminati", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                List<JurusanDTO> jurusanList = Arrays.stream(text.split(","))
                    .map(id -> {
                        JurusanDTO jurusanDTO = new JurusanDTO();
                        jurusanDTO.setId(Long.parseLong(id));
                        return jurusanDTO;
                    })
                    .collect(Collectors.toList());
                setValue(jurusanList);
            }
        });
    }

    @GetMapping("/hasilTest")
    public String lihatHasilTest(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"USER".equals(user.getRole())) {
            return "redirect:/login"; // Redirect jika user belum login atau tidak memiliki peran USER
        }

        CalonMahasiswa calonMahasiswa = calonMahasiswaService.findByUser(user);
        if (calonMahasiswa == null) {
            model.addAttribute("errorMessage", "Mohon masukkan biodata terlebih dahulu dan memilih jurusan.");
            return "calonMahasiswa/inputBiodata";
        }

        // Periksa apakah biodata sudah diisi
        if (calonMahasiswa.getNik() == null) {
            calonMahasiswa.setNik("NIK belum diisi");
        }
        if (calonMahasiswa.getNama() == null) {
            calonMahasiswa.setNama("Nama belum diisi");
        }
        if (calonMahasiswa.getNotelp() == null) {
            calonMahasiswa.setNotelp("No. Telp belum diisi");
        }
        if (calonMahasiswa.getJurusanYangDiminati().isEmpty()) {
            model.addAttribute("errorMessage", "Belum memilih jurusan.");
        }
        if (calonMahasiswa.getHasilTes() == null) {
            calonMahasiswa.setHasilTes(new HasilTes());
            calonMahasiswa.getHasilTes().setTotalNilai(0);
            calonMahasiswa.getHasilTes().setNilaiA(0);
            calonMahasiswa.getHasilTes().setNilaiB(0);
            calonMahasiswa.getHasilTes().setNilaiC(0);
        }

        model.addAttribute("calonMahasiswa", calonMahasiswa);
        return "calonMahasiswa/lihatHasilTest";
    }


    

   @GetMapping("/profile")
public String mahasiswaProfile(HttpSession session, Model model) {
    // Ambil user dari session
    User user = (User) session.getAttribute("user");
    if (user == null || !"USER".equals(user.getRole())) {
        return "redirect:/login"; 
    }

    // Cari data calon mahasiswa berdasarkan user
    CalonMahasiswa calonMahasiswa = calonMahasiswaService.findByUser(user);
    if (calonMahasiswa == null) {
        // Jika data calon mahasiswa tidak ditemukan, buat objek kosong dengan pesan default
        calonMahasiswa = new CalonMahasiswa();
        calonMahasiswa.setNama("Nama belum diisi");
        calonMahasiswa.setNik("NIK belum diisi");
        calonMahasiswa.setNotelp("No. Telp belum diisi");
    }

    // Tambahkan data ke model
    model.addAttribute("calonMahasiswa", calonMahasiswa);
    model.addAttribute("user", user);
    return "calonMahasiswa/profile"; // Return template profile
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
