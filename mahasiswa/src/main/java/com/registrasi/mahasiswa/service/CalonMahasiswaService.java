package com.registrasi.mahasiswa.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.HasilTes;
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
        calonMahasiswa = calonMahasiswaRepository.save(calonMahasiswa);
        return convertToDTO(calonMahasiswa);
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
        calonMahasiswaDTO.setJurusanYangDiminati(calonMahasiswa.getJurusanYangDiminati());
        calonMahasiswaDTO.setStatusPenerimaan(calonMahasiswa.getStatusPenerimaan());
        if (calonMahasiswa.getHasilTes() != null) {
            calonMahasiswaDTO.setHasilTesId(calonMahasiswa.getHasilTes().getId());
            calonMahasiswaDTO.setTotalNilai(calonMahasiswa.getHasilTes().getTotalNilai());
        }
        return calonMahasiswaDTO;
    }

    public CalonMahasiswa convertToEntity(CalonMahasiswaDTO calonMahasiswaDTO) {
        CalonMahasiswa calonMahasiswa = new CalonMahasiswa();
        calonMahasiswa.setId(calonMahasiswaDTO.getId());
        calonMahasiswa.setNik(calonMahasiswaDTO.getNik());
        calonMahasiswa.setNama(calonMahasiswaDTO.getNama());
        calonMahasiswa.setNotelp(calonMahasiswaDTO.getNotelp());
        calonMahasiswa.setJurusanYangDiminati(calonMahasiswaDTO.getJurusanYangDiminati());
        calonMahasiswa.setStatusPenerimaan(calonMahasiswaDTO.getStatusPenerimaan());
        calonMahasiswa.setUser(calonMahasiswaDTO.getUser());
        if (calonMahasiswaDTO.getHasilTesId() != null) {
            HasilTes hasilTes = new HasilTes();
            hasilTes.setId(calonMahasiswaDTO.getHasilTesId());
            calonMahasiswa.setHasilTes(hasilTes);
        }
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
}
