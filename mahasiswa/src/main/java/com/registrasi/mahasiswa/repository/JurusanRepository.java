package com.registrasi.mahasiswa.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.registrasi.mahasiswa.model.Jurusan;

public interface JurusanRepository extends JpaRepository<Jurusan, Long> {
    boolean existsByNamaJurusan(String namaJurusan);
}
