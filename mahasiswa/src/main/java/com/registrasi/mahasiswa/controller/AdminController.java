package com.registrasi.mahasiswa.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.dto.HasilTesDTO;
import com.registrasi.mahasiswa.dto.JurusanDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.Jurusan;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.service.CalonMahasiswaService;
import com.registrasi.mahasiswa.service.HasilTesService;
import com.registrasi.mahasiswa.service.JurusanService;
import com.registrasi.mahasiswa.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private JurusanService jurusanService;

    @Autowired
    private HasilTesService hasilTesService;

    @Autowired private 
    UserService userService;

    
    @Autowired
    private CalonMahasiswaService calonMahasiswaService;

    // Helper method to check if the user is an admin 
    private boolean isAdmin(HttpSession session) { 
        User user = (User) session.getAttribute("user"); 
        return user != null && "ADMIN".equals(user.getRole()); 
    }

    // Menampilkan daftar jurusan
    @GetMapping("/jurusan") 
    public String listJurusan(HttpSession session, Model model) { 
        if (!isAdmin(session)) { 
            return "redirect:/login"; 
        } 
        model.addAttribute("jurusanList", jurusanService.getAllJurusan()); return "admin/listJurusan"; 
    }

    // Menampilkan form untuk membuat jurusan baru
    @GetMapping("/jurusan/create")
    public String createJurusanForm(HttpSession session, Model model) {
        if (!isAdmin(session)){
            return "redirect:/login";

        }
        model.addAttribute("jurusan", new JurusanDTO());
        return "admin/createJurusan";
    }

  // Menangani proses penyimpanan jurusan baru
    @PostMapping("/jurusan")
    public String createJurusan(
            @Valid @ModelAttribute JurusanDTO jurusanDTO, 
            BindingResult bindingResult, 
            Model model) {
        if (bindingResult.hasErrors()) {
            // Jika ada error validasi, kembali ke form
            return "admin/createJurusan";
        }
        if (jurusanService.existsByNama(jurusanDTO.getNamaJurusan())) {
            // Jika nama jurusan sudah ada, tampilkan pesan error
            model.addAttribute("message", "Jurusan sudah ada");
            return "admin/createJurusan";
        }
        // Simpan jurusan jika tidak ada error
        jurusanService.saveJurusan(jurusanDTO);
        model.addAttribute("successMessage", "Jurusan berhasil dibuat");
        return "redirect:/admin/jurusan";
    }


    @GetMapping("/jurusan/update/{id}") 
    public String updateJurusanForm(@PathVariable Long id, HttpSession session, Model model) { 
    if (!isAdmin(session)) { 
        return "redirect:/login"; 
    }
    JurusanDTO jurusanDTO = jurusanService.getAllJurusan().stream().filter(j -> j.getId().equals(id)).findFirst().orElse(null); 
    model.addAttribute("jurusan", jurusanDTO); 
        return "admin/updateJurusan"; 
    } 
    
    @PostMapping("/jurusan/update") 
    public String updateJurusan(@ModelAttribute JurusanDTO jurusanDTO) { 
        jurusanService.updateJurusan(jurusanDTO); 
        return "redirect:/admin/jurusan"; 
    }

    @GetMapping("/jurusan/delete/{id}")
    public String deleteJurusan(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)){
            return "redirect:/login";
        }
        jurusanService.deleteJurusan(id);
        return "redirect:/admin/jurusan";
    }
    // jurusan end

   
    @GetMapping("/hasilTes")
    public String hasilTesForm(Model model, HttpSession session) {
        if(!isAdmin(session)){
            return "redirect:/login";
        }
        model.addAttribute("hasilTes", new HasilTesDTO());
        return "admin/hasilTesForm";
    }
    
    @PostMapping("/hasilTes")
    public String cariMahasiswa(@RequestParam String nik, Model model) {
        CalonMahasiswa mahasiswa = calonMahasiswaService.findByNik(nik);
        if (mahasiswa != null) {
            HasilTesDTO hasilTesDTO = new HasilTesDTO();
            hasilTesDTO.setNik(mahasiswa.getNik());
            hasilTesDTO.setNama(mahasiswa.getNama());
            model.addAttribute("mahasiswa", mahasiswa);
            model.addAttribute("hasilTes", hasilTesDTO);
        } else {
            model.addAttribute("error", "Mahasiswa tidak ditemukan");
        }
        return "admin/hasilTesForm";
    }
    
    @PostMapping("/hasilTes/save")
    public String saveHasilTes(@ModelAttribute HasilTesDTO hasilTesDTO, Model model) {
        // Periksa apakah NIK sudah ada di database
        if (hasilTesService.existsByNik(hasilTesDTO.getNik())) {
            model.addAttribute("error", "NIK tersebut sudah diinputkan nilainya");
        } else {
            // Simpan hasil tes
            HasilTesDTO savedHasilTes = hasilTesService.saveHasilTes(hasilTesDTO);
    
            // Cari data CalonMahasiswa berdasarkan NIK
            CalonMahasiswa mahasiswa = calonMahasiswaService.findByNik(savedHasilTes.getNik());
            if (mahasiswa != null) {
                // Konversi CalonMahasiswa ke CalonMahasiswaDTO
                CalonMahasiswaDTO calonMahasiswaDTO = calonMahasiswaService.convertToDTO(mahasiswa);
    
                // Hubungkan hasil tes ke CalonMahasiswaDTO
                calonMahasiswaDTO.setHasilTesId(savedHasilTes.getId());
    
                // Simpan perubahan pada CalonMahasiswa
                calonMahasiswaService.saveCalonMahasiswa(calonMahasiswaDTO, mahasiswa.getUser());
            }
    
            // Tambahkan pesan sukses
            model.addAttribute("message", "Hasil tes berhasil disimpan!");
        }
    
        // Kembalikan ke halaman form hasil tes
        return "admin/hasilTesForm";
    }
    

    // hasil test end
    @GetMapping("/logout") 
    public String logout(HttpServletRequest request, Model model) { 
        HttpSession session = request.getSession(false); 
        if (session != null) { 
            session.invalidate(); 
        } 
        model.addAttribute("message", "Anda telah berhasil logout."); 
        return "admin/logout"; 
    }


    // Profile 
    @GetMapping("/profile") 
     public String adminProfile(HttpSession session, Model model) { 
        User user = (User) session.getAttribute("user"); 
        if (user == null || !"ADMIN".equals(user.getRole())) { 
            return "redirect:/login"; 
        } 
        model.addAttribute("user", user); 
        return "admin/profile"; 
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
    return "redirect:/admin/profile";
    }
    // Profile Ends


    @GetMapping("/mahasiswa/jurusan-diterima")
    public String listMahasiswaWithFilters(
            @RequestParam(value = "jurusan", required = false) Long jurusanId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "nama", required = false) String nama,
            Model model
    ) {
        // Mendapatkan daftar jurusan (semua jurusan)
        List<Jurusan> jurusanList = jurusanService.findAll();

        // Mendapatkan data mahasiswa yang sudah difilter
        List<CalonMahasiswa> mahasiswaList = calonMahasiswaService.findAllWithFilters(jurusanId, status, nama);

        // Menambahkan data ke model
        model.addAttribute("mahasiswaList", mahasiswaList);
        model.addAttribute("jurusanList", jurusanList);

        // Mengembalikan nama template
        return "admin/listMahasiswa"; // Sesuaikan dengan template HTML yang digunakan
    }

}