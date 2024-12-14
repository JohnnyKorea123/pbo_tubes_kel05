package com.registrasi.mahasiswa.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registrasi.mahasiswa.model.Jurusan;

public interface JurusanRepository extends JpaRepository<Jurusan, Long> {
    boolean existsByNamaJurusan(String namaJurusan);
    
    @Override
     public List<Jurusan> findAll();
}
