package com.registrasi.mahasiswa.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.registrasi.mahasiswa.dto.HasilTesDTO;
import com.registrasi.mahasiswa.dto.JurusanDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.service.CalonMahasiswaService;
import com.registrasi.mahasiswa.service.HasilTesService;
import com.registrasi.mahasiswa.service.JurusanService;
import com.registrasi.mahasiswa.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/jurusan")
    public String listJurusan(Model model) {
        model.addAttribute("jurusanList", jurusanService.getAllJurusan());
        return "admin/listJurusan";
    }

    @GetMapping("/jurusan/create")
    public String createJurusanForm(Model model) {
        model.addAttribute("jurusan", new JurusanDTO());
        return "admin/createJurusan";
    }

    @PostMapping("/jurusan") 
    public String createJurusan(@ModelAttribute JurusanDTO jurusanDTO, Model model) { 
        if (jurusanService.existsByNama(jurusanDTO.getNamaJurusan())) { 
            model.addAttribute("error", "Jurusan sudah ada"); return "admin/createJurusan"; 
        } 
        jurusanService.saveJurusan(jurusanDTO); 
        return "redirect:/admin/jurusan"; 
    }

    @GetMapping("/jurusan/update/{id}") 
    public String updateJurusanForm(@PathVariable Long id, Model model) { 
    JurusanDTO jurusanDTO = jurusanService.getAllJurusan().stream().filter(j -> j.getId().equals(id)).findFirst().orElse(null); 
    model.addAttribute("jurusan", jurusanDTO); 
        return "admin/updateJurusan"; 
    } 
    
    @PostMapping("/jurusan/update") 
    public String updateJurusan(@ModelAttribute JurusanDTO jurusanDTO) { 
        jurusanService.updateJurusan(jurusanDTO); 
        return "redirect:/admin/jurusan"; 
    }



    // @PostMapping("/jurusan")
    // public String createJurusan(@ModelAttribute JurusanDTO jurusanDTO) {
    //     jurusanService.saveJurusan(jurusanDTO);
    //     return "redirect:/admin/jurusan";
    // }

    // @GetMapping("/jurusan/update/{id}")
    // public String updateJurusanForm(@PathVariable Long id, Model model) {
    //     model.addAttribute("jurusan", jurusanService.getAllJurusan().stream().filter(j -> j.getId().equals(id)).findFirst().orElse(null));
    //     return "admin/updateJurusan";
    // }

    // @PostMapping("/jurusan/update")
    // public String updateJurusan(@ModelAttribute JurusanDTO jurusanDTO) {
    //     jurusanService.updateJurusan(jurusanDTO);
    //     return "redirect:/admin/jurusan";
    // }

    @GetMapping("/jurusan/delete/{id}")
    public String deleteJurusan(@PathVariable Long id) {
        jurusanService.deleteJurusan(id);
        return "redirect:/admin/jurusan";
    }


    @GetMapping("/mahasiswa")
    public String listMahasiswaByRole(Model model) { 
        List<User> userList = userService.findByRole("USER"); model.addAttribute("userList", userList); 
        return "admin/listMahasiswa"; 
    }

    @GetMapping("/mahasiswa/{status}")
    public String listMahasiswaByStatus(@PathVariable String status, Model model) {
        boolean isLulus = status.equalsIgnoreCase("lulus");
        model.addAttribute("mahasiswaList", hasilTesService.findAll().stream().filter(m -> (m.getTotalNilai() >= 60) == isLulus).toList());
        return "admin/listMahasiswa";
    }

    // @GetMapping("/register") 
    // public String registerForm(Model model) { 
    //     model.addAttribute("user", new UserDTO()); 
    //     return "admin/register"; 
    // }

    // @PostMapping("/register") public String register(@ModelAttribute UserDTO userDTO, Model model) { 
    //     userService.saveUser(userDTO); model.addAttribute("message", "Akun berhasil didaftarkan!"); 
    //     return "admin/register";
    // }


    // @GetMapping("/listMahasiswa") 
    // public String listMahasiswaByRole(Model model) { 
    //     List<User> userList = userService.findByRole("USER"); model.addAttribute("userList", userList); 
    //     return "admin/listMahasiswa"; 
    // }



    


    @GetMapping("/hasilTes")
    public String hasilTesForm(Model model) {
        model.addAttribute("hasilTes", new HasilTesDTO());
        return "admin/hasilTesForm";
    }

    @PostMapping("/hasilTes")
    public String cariMahasiswa(@RequestParam String nik, @RequestParam String nama, Model model) {
        CalonMahasiswa mahasiswa = calonMahasiswaService.findByNikAndNama(nik, nama);
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
        hasilTesService.saveHasilTes(hasilTesDTO);
        model.addAttribute("message", "Hasil tes berhasil disimpan!");
        return "admin/hasilTesForm";
    }


    @GetMapping("/logout") 
    public String logout(HttpServletRequest request, Model model) { 
        HttpSession session = request.getSession(false); 
        if (session != null) { 
            session.invalidate(); 
        } 
        model.addAttribute("message", "Anda telah berhasil logout."); 
        return "admin/logout"; }
}