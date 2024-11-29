package com.registrasi.mahasiswa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registrasi.mahasiswa.dto.CalonMahasiswaDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.User;
import com.registrasi.mahasiswa.repository.CalonMahasiswaRepository;

@Service
public class CalonMahasiswaService {
    @Autowired
    private CalonMahasiswaRepository calonMahasiswaRepository;

    public CalonMahasiswaDTO saveCalonMahasiswa(CalonMahasiswaDTO calonMahasiswaDTO, User user) {
        CalonMahasiswa calonMahasiswa = convertToEntity(calonMahasiswaDTO);
        calonMahasiswa.setUser(user);
        calonMahasiswa = calonMahasiswaRepository.save(calonMahasiswa);
        return convertToDTO(calonMahasiswa);
    }

    public List<CalonMahasiswaDTO> findAll() {
        List<CalonMahasiswa> calonMahasiswaList = calonMahasiswaRepository.findAll();
        return calonMahasiswaList.stream().map(this::convertToDTO).toList();
    }

    public CalonMahasiswa findByNikAndNama(String nik, String nama) { 
        return calonMahasiswaRepository.findByNikAndNama(nik, nama); 
    }

    public CalonMahasiswa findByUser(User user) {
        return calonMahasiswaRepository.findByUser(user);
    }

    private CalonMahasiswaDTO convertToDTO(CalonMahasiswa calonMahasiswa) {
        CalonMahasiswaDTO calonMahasiswaDTO = new CalonMahasiswaDTO();
        calonMahasiswaDTO.setId(calonMahasiswa.getId());
        calonMahasiswaDTO.setNik(calonMahasiswa.getNik());
        calonMahasiswaDTO.setNama(calonMahasiswa.getNama());
        calonMahasiswaDTO.setNotelp(calonMahasiswa.getNotelp());
        calonMahasiswaDTO.setJurusanYangDiminati(calonMahasiswa.getJurusanYangDiminati());
        calonMahasiswaDTO.setHasilTest(calonMahasiswa.getHasilTest());
        calonMahasiswaDTO.setStatusPenerimaan(calonMahasiswa.getStatusPenerimaan());
        return calonMahasiswaDTO;
    }

    private CalonMahasiswa convertToEntity(CalonMahasiswaDTO calonMahasiswaDTO) {
        CalonMahasiswa calonMahasiswa = new CalonMahasiswa();
        calonMahasiswa.setId(calonMahasiswaDTO.getId());
        calonMahasiswa.setNik(calonMahasiswaDTO.getNik());
        calonMahasiswa.setNama(calonMahasiswaDTO.getNama());
        calonMahasiswa.setNotelp(calonMahasiswaDTO.getNotelp());
        calonMahasiswa.setJurusanYangDiminati(calonMahasiswaDTO.getJurusanYangDiminati());
        calonMahasiswa.setHasilTest(calonMahasiswaDTO.getHasilTest());
        calonMahasiswa.setStatusPenerimaan(calonMahasiswaDTO.getStatusPenerimaan());
        calonMahasiswa.setUser(calonMahasiswaDTO.getUser());
        return calonMahasiswa;
    }
}
