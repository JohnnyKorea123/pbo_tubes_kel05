package com.registrasi.mahasiswa.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.registrasi.mahasiswa.model.HasilTes;

public interface HasilTesRepository extends JpaRepository<HasilTes, Long> {
    HasilTes findByNik(String nik);
    boolean existsByNik(String nik);
}
