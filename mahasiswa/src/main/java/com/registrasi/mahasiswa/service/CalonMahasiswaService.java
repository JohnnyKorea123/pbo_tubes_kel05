package com.registrasi.mahasiswa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.HasilTes;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.repository.CalonMahasiswaRepository;
import com.registrasi.mahasiswa.repository.HasilTesRepository;
@Service
public class CalonMahasiswaService {
    @Autowired
    private CalonMahasiswaRepository calonMahasiswaRepository;

    @Autowired
    private HasilTesRepository hasilTesRepository;

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
        calonMahasiswaDTO.setJurusan(calonMahasiswa.getJurusan());
        if (calonMahasiswa.getHasilTes() != null) {
            calonMahasiswaDTO.setHasilTesId(calonMahasiswa.getHasilTes().getId()); // Set hasilTesId
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
        calonMahasiswa.setJurusan(calonMahasiswaDTO.getJurusan());
        if (calonMahasiswaDTO.getHasilTesId() != null) {
            HasilTes hasilTes = new HasilTes();
            hasilTes.setId(calonMahasiswaDTO.getHasilTesId());
            calonMahasiswa.setHasilTes(hasilTes); // Set hasilTes
        }
        return calonMahasiswa;
    }
}
