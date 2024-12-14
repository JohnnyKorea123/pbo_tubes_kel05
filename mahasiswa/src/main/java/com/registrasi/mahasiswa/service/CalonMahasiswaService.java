package com.registrasi.mahasiswa.service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.dto.JurusanDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.HasilTes;
import com.registrasi.mahasiswa.model.Jurusan;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.repository.CalonMahasiswaRepository;
import com.registrasi.mahasiswa.repository.HasilTesRepository;
import com.registrasi.mahasiswa.repository.UserRepository;



@Service
public class CalonMahasiswaService {
    @Autowired
    private CalonMahasiswaRepository calonMahasiswaRepository;

    @Autowired
    private HasilTesRepository hasilTesRepository;

    
    @Autowired
    private UserRepository userRepository;

    public CalonMahasiswaDTO saveCalonMahasiswa(CalonMahasiswaDTO calonMahasiswaDTO, User user) {
        CalonMahasiswa calonMahasiswa = convertToEntity(calonMahasiswaDTO);
        calonMahasiswa.setUser(user);
        if (calonMahasiswaDTO.getHasilTesId() != null) {
            HasilTes hasilTes = hasilTesRepository.findById(calonMahasiswaDTO.getHasilTesId()).orElse(null);
            calonMahasiswa.setHasilTes(hasilTes);
        }
        Jurusan jurusanYangDiterima = tentukanJurusanYangDiterima(calonMahasiswaDTO);
        calonMahasiswa.setStatusPenerimaan(jurusanYangDiterima != null ? "Diterima" : "Tidak Diterima");
        calonMahasiswa = calonMahasiswaRepository.save(calonMahasiswa);
        return convertToDTO(calonMahasiswa);
    }

    public Jurusan tentukanJurusanYangDiterima(CalonMahasiswaDTO calonMahasiswaDTO) {
        List<JurusanDTO> jurusanYangDiminati = calonMahasiswaDTO.getJurusanYangDiminati();
        for (JurusanDTO jurusan : jurusanYangDiminati) {
            if (calonMahasiswaDTO.getTotalNilai() >= jurusan.getSyaratNilai()) {
                return convertToEntity(jurusan);
            }
        }
        return null; // Mengembalikan null jika tidak ada jurusan yang memenuhi syarat
    }
    
    private Jurusan convertToEntity(JurusanDTO jurusanDTO) {
        Jurusan jurusan = new Jurusan();
        jurusan.setId(jurusanDTO.getId());
        jurusan.setNamaJurusan(jurusanDTO.getNamaJurusan());
        jurusan.setSyaratNilai(jurusanDTO.getSyaratNilai());
        return jurusan;
    }
    
    

    public List<CalonMahasiswaDTO> findAll() {
        List<CalonMahasiswa> calonMahasiswaList = calonMahasiswaRepository.findAll();
        return calonMahasiswaList.stream().map(this::convertToDTO).toList();
    }

    public CalonMahasiswa findByNik(String nik) {
        return calonMahasiswaRepository.findByNik(nik);
    }

    public CalonMahasiswa findByUser(User user) {
        return calonMahasiswaRepository.findByUser(user);
    }

    public CalonMahasiswaDTO convertToDTO(CalonMahasiswa calonMahasiswa) {
        CalonMahasiswaDTO calonMahasiswaDTO = new CalonMahasiswaDTO();
        calonMahasiswaDTO.setId(calonMahasiswa.getId());
        calonMahasiswaDTO.setNik(calonMahasiswa.getNik());
        calonMahasiswaDTO.setNama(calonMahasiswa.getNama());
        calonMahasiswaDTO.setNotelp(calonMahasiswa.getNotelp());
        calonMahasiswaDTO.setStatusPenerimaan(calonMahasiswa.getStatusPenerimaan());
        if (calonMahasiswa.getHasilTes() != null) {
            calonMahasiswaDTO.setHasilTesId(calonMahasiswa.getHasilTes().getId());
            calonMahasiswaDTO.setTotalNilai(calonMahasiswa.getHasilTes().getTotalNilai());
        }
        List<JurusanDTO> jurusanYangDiminati = calonMahasiswa.getJurusanYangDiminati().stream()
            .map(jurusan -> {
                JurusanDTO jurusanDTO = new JurusanDTO();
                jurusanDTO.setId(jurusan.getId());
                jurusanDTO.setNamaJurusan(jurusan.getNamaJurusan());
                jurusanDTO.setSyaratNilai(jurusan.getSyaratNilai());
                return jurusanDTO;
            })
            .collect(Collectors.toList());
        calonMahasiswaDTO.setJurusanYangDiminati(jurusanYangDiminati);
        return calonMahasiswaDTO;
    }

    public CalonMahasiswa convertToEntity(CalonMahasiswaDTO calonMahasiswaDTO) {
        CalonMahasiswa calonMahasiswa = new CalonMahasiswa();
        calonMahasiswa.setId(calonMahasiswaDTO.getId());
        calonMahasiswa.setNik(calonMahasiswaDTO.getNik());
        calonMahasiswa.setNama(calonMahasiswaDTO.getNama());
        calonMahasiswa.setNotelp(calonMahasiswaDTO.getNotelp());
        calonMahasiswa.setStatusPenerimaan(calonMahasiswaDTO.getStatusPenerimaan());
        if (calonMahasiswaDTO.getHasilTesId() != null) {
            HasilTes hasilTes = new HasilTes();
            hasilTes.setId(calonMahasiswaDTO.getHasilTesId());
            calonMahasiswa.setHasilTes(hasilTes);
        }
        List<Jurusan> jurusanYangDiminati = calonMahasiswaDTO.getJurusanYangDiminati().stream()
            .map(jurusanDTO -> {
                Jurusan jurusan = new Jurusan();
                jurusan.setId(jurusanDTO.getId());
                jurusan.setNamaJurusan(jurusanDTO.getNamaJurusan());
                jurusan.setSyaratNilai(jurusanDTO.getSyaratNilai());
                return jurusan;
            })
            .collect(Collectors.toList());
        calonMahasiswa.setJurusanYangDiminati(jurusanYangDiminati);
        return calonMahasiswa;
    }

    public void saveProfilePicture(User user, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get("uploads/profile-pictures");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture("/uploads/profile-pictures/" + fileName);
            userRepository.save(user);
        } catch (IOException e) {
            throw new IOException("Gagal menyimpan file: " + fileName, e);
        }
    }

    public List<CalonMahasiswa> findAllWithJurusanDiterima() {
        return calonMahasiswaRepository.findAllWithJurusanDiterima();
    }



    public List<CalonMahasiswa> findAllWithFilters(Long jurusanId, String status, String nama) {
        List<CalonMahasiswa> mahasiswaList = calonMahasiswaRepository.findAll();
    
        return mahasiswaList.stream()
            .filter(mahasiswa -> {
                // Filter berdasarkan jurusan diterima
                if (jurusanId != null) {
                    // Hanya periksa jika jurusan diterima tersedia
                    if (mahasiswa.getJurusanDiterima() == null || 
                        !mahasiswa.getJurusanDiterima().getJurusan().getId().equals(jurusanId)) {
                        return false;
                    }
                }
    
                // Filter berdasarkan status
                if (status != null && !status.isEmpty()) {
                    boolean diterima = mahasiswa.getJurusanDiterima() != null;
                    boolean statusMatch = (status.equalsIgnoreCase("lulus") && diterima) ||
                                          (status.equalsIgnoreCase("tidak_lulus") && !diterima);
                    if (!statusMatch) {
                        return false;
                    }
                }
    
                // Filter berdasarkan nama
                if (nama != null && !nama.isEmpty()) {
                    if (!mahasiswa.getNama().toLowerCase().contains(nama.toLowerCase())) {
                        return false;
                    }
                }
    
                return true; // Lulus semua filter
            })
            .collect(Collectors.toList());
    }


   

    
}
