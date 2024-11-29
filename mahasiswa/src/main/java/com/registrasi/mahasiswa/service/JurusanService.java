package com.registrasi.mahasiswa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registrasi.mahasiswa.dto.JurusanDTO;
import com.registrasi.mahasiswa.model.Jurusan;
import com.registrasi.mahasiswa.repository.JurusanRepository;

@Service
public class JurusanService {
    @Autowired
    private JurusanRepository jurusanRepository;

    public JurusanDTO saveJurusan(JurusanDTO jurusanDTO) {
        Jurusan jurusan = convertToEntity(jurusanDTO);
        jurusan = jurusanRepository.save(jurusan);
        return convertToDTO(jurusan);
    }


    public boolean existsByNama(String nama) { 
        return jurusanRepository.existsByNamaJurusan(nama); 
    }

    public JurusanDTO updateJurusan(JurusanDTO jurusanDTO) { 
        Jurusan jurusan = convertToEntity(jurusanDTO); 
        jurusan = jurusanRepository.save(jurusan); 
        return convertToDTO(jurusan); 
    }

    public void deleteJurusan(Long id) {
        jurusanRepository.deleteById(id);
    }

    public List<JurusanDTO> getAllJurusan() {
        List<Jurusan> jurusanList = jurusanRepository.findAll();
        return jurusanList.stream().map(this::convertToDTO).toList();
    }

    private JurusanDTO convertToDTO(Jurusan jurusan) {
        JurusanDTO jurusanDTO = new JurusanDTO();
        jurusanDTO.setId(jurusan.getId());
        jurusanDTO.setNamaJurusan(jurusan.getNamaJurusan());
        return jurusanDTO;
    }

    private Jurusan convertToEntity(JurusanDTO jurusanDTO) {
        Jurusan jurusan = new Jurusan();
        jurusan.setId(jurusanDTO.getId());
        jurusan.setNamaJurusan(jurusanDTO.getNamaJurusan());
        return jurusan;
    }
}
