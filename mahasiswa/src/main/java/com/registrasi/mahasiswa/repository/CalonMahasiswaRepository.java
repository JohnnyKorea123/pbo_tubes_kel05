package com.registrasi.mahasiswa.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.User;

public interface CalonMahasiswaRepository extends JpaRepository<CalonMahasiswa, Long> {
    CalonMahasiswa findByNik(String nik);
    CalonMahasiswa findByUser(User user);
}
