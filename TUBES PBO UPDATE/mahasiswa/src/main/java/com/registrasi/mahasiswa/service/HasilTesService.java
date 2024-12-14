package com.registrasi.mahasiswa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registrasi.mahasiswa.dto.HasilTesDTO;
import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.HasilTes;
import com.registrasi.mahasiswa.model.Jurusan;
import com.registrasi.mahasiswa.model.JurusanDiterima;
import com.registrasi.mahasiswa.repository.CalonMahasiswaRepository;
import com.registrasi.mahasiswa.repository.HasilTesRepository;
import com.registrasi.mahasiswa.repository.JurusanDiterimaRepository;

@Service
public class HasilTesService {
    @Autowired
    private HasilTesRepository hasilTesRepository;

    @Autowired
    private CalonMahasiswaRepository calonMahasiswaRepository;


    @Autowired
    private JurusanDiterimaRepository jurusanDiterimaRepository;

    public HasilTesDTO saveHasilTes(HasilTesDTO hasilTesDTO) {
        HasilTes hasilTes = convertToEntity(hasilTesDTO);
        hasilTes.setTotalNilai(hasilTes.getNilaiA() + hasilTes.getNilaiB() + hasilTes.getNilaiC());
        hasilTes = hasilTesRepository.save(hasilTes);

        // Update CalonMahasiswa with HasilTes
        CalonMahasiswa calonMahasiswa = calonMahasiswaRepository.findByNik(hasilTes.getNik());
        if (calonMahasiswa != null) {
            calonMahasiswa.setHasilTes(hasilTes);
            calonMahasiswaRepository.save(calonMahasiswa);

            // Determine Jurusan yang Diterima
            Jurusan jurusanYangDiterima = tentukanJurusanYangDiterima(calonMahasiswa);
            if (jurusanYangDiterima != null) {
                JurusanDiterima jurusanDiterima = new JurusanDiterima();
                jurusanDiterima.setCalonMahasiswa(calonMahasiswa);
                jurusanDiterima.setJurusan(jurusanYangDiterima);
                jurusanDiterimaRepository.save(jurusanDiterima);
            }
        }

        return convertToDTO(hasilTes);
    }

    public List<HasilTesDTO> findAll() {
        List<HasilTes> hasilTesList = hasilTesRepository.findAll();
        return hasilTesList.stream().map(this::convertToDTO).toList();
    }

    public HasilTesDTO findByNik(String nik) {
        HasilTes hasilTes = hasilTesRepository.findByNik(nik);
        return convertToDTO(hasilTes);
    }

    public boolean existsByNik(String nik) {
        return hasilTesRepository.existsByNik(nik);
    }

    private HasilTesDTO convertToDTO(HasilTes hasilTes) {
        HasilTesDTO hasilTesDTO = new HasilTesDTO();
        hasilTesDTO.setId(hasilTes.getId());
        hasilTesDTO.setNik(hasilTes.getNik());
        hasilTesDTO.setNama(hasilTes.getNama());
        hasilTesDTO.setJurusanYangDiminati(hasilTes.getJurusanYangDiminati());
        hasilTesDTO.setNilaiA(hasilTes.getNilaiA());
        hasilTesDTO.setNilaiB(hasilTes.getNilaiB());
        hasilTesDTO.setNilaiC(hasilTes.getNilaiC());
        hasilTesDTO.setTotalNilai(hasilTes.getTotalNilai());
        return hasilTesDTO;
    }

    private HasilTes convertToEntity(HasilTesDTO hasilTesDTO) {
        HasilTes hasilTes = new HasilTes();
        hasilTes.setId(hasilTesDTO.getId());
        hasilTes.setNik(hasilTesDTO.getNik());
        hasilTes.setNama(hasilTesDTO.getNama());
        hasilTes.setJurusanYangDiminati(hasilTesDTO.getJurusanYangDiminati());
        hasilTes.setNilaiA(hasilTesDTO.getNilaiA());
        hasilTes.setNilaiB(hasilTesDTO.getNilaiB());
        hasilTes.setNilaiC(hasilTesDTO.getNilaiC());
        hasilTes.setTotalNilai(hasilTesDTO.getTotalNilai());
        return hasilTes;
    }

    private Jurusan tentukanJurusanYangDiterima(CalonMahasiswa calonMahasiswa) {
        for (Jurusan jurusan : calonMahasiswa.getJurusanYangDiminati()) {
            if (calonMahasiswa.getHasilTes().getTotalNilai() >= jurusan.getSyaratNilai()) {
                return jurusan;
            }
        }
        return null;
    }
}
