package com.registrasi.mahasiswa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registrasi.mahasiswa.dto.JurusanDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.Jurusan;
import com.registrasi.mahasiswa.model.JurusanDiterima;
import com.registrasi.mahasiswa.repository.CalonMahasiswaRepository;
import com.registrasi.mahasiswa.repository.JurusanRepository;

@Service
public class JurusanService {
    @Autowired
    private JurusanRepository jurusanRepository;

    @Autowired
    private CalonMahasiswaRepository calonMahasiswaRepository;

    public JurusanDTO saveJurusan(JurusanDTO jurusanDTO) {
        Jurusan jurusan = convertToEntity(jurusanDTO);
        jurusan = jurusanRepository.save(jurusan);
        syncMahasiswaJurusan();
        return convertToDTO(jurusan);
    }

    public boolean existsByNama(String nama) { 
        return jurusanRepository.existsByNamaJurusan(nama); 
    }

    public JurusanDTO updateJurusan(JurusanDTO jurusanDTO) { 
        Jurusan jurusan = convertToEntity(jurusanDTO); 
        jurusan = jurusanRepository.save(jurusan); 
        syncMahasiswaJurusan();
        return convertToDTO(jurusan); 
    }

    public void deleteJurusan(Long id) {
        jurusanRepository.deleteById(id);
        syncMahasiswaJurusan();
    }

    public List<JurusanDTO> getAllJurusan() {
        List<Jurusan> jurusanList = jurusanRepository.findAll();
        return jurusanList.stream().map(this::convertToDTO).toList();
    }

    private JurusanDTO convertToDTO(Jurusan jurusan) {
        JurusanDTO jurusanDTO = new JurusanDTO();
        jurusanDTO.setId(jurusan.getId());
        jurusanDTO.setNamaJurusan(jurusan.getNamaJurusan());
        jurusanDTO.setSyaratNilai(jurusan.getSyaratNilai());
        return jurusanDTO;
    }

    private Jurusan convertToEntity(JurusanDTO jurusanDTO) {
        Jurusan jurusan = new Jurusan();
        jurusan.setId(jurusanDTO.getId());
        jurusan.setNamaJurusan(jurusanDTO.getNamaJurusan());
        jurusan.setSyaratNilai(jurusanDTO.getSyaratNilai());
        return jurusan;
    }

    private void syncMahasiswaJurusan() {
        List<CalonMahasiswa> mahasiswaList = calonMahasiswaRepository.findAll();
        List<Jurusan> jurusanList = jurusanRepository.findAll();
        Map<String, Jurusan> jurusanMap = jurusanList.stream()
            .collect(Collectors.toMap(Jurusan::getNamaJurusan, Function.identity()));

        for (CalonMahasiswa mahasiswa : mahasiswaList) {
            List<Jurusan> updatedJurusanList = new ArrayList<>();
            for (Jurusan jurusan : mahasiswa.getJurusanYangDiminati()) {
                if (jurusanMap.containsKey(jurusan.getNamaJurusan())) {
                    updatedJurusanList.add(jurusanMap.get(jurusan.getNamaJurusan()));
                }
            }
            mahasiswa.setJurusanYangDiminati(updatedJurusanList);
            calonMahasiswaRepository.save(mahasiswa);
        }
    }

     public List<Jurusan> findAllJurusanDiterima() {
    return calonMahasiswaRepository.findAll().stream()
        .map(CalonMahasiswa::getJurusanDiterima)
        .filter(Objects::nonNull)
        .map(JurusanDiterima::getJurusan)
        .distinct()
        .collect(Collectors.toList());
    }

    public List<Jurusan> findAll() {
        return jurusanRepository.findAll();
    }


}

