package com.registrasi.mahasiswa.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.registrasi.mahasiswa.model.CalonMahasiswa;
import com.registrasi.mahasiswa.model.User;

public interface CalonMahasiswaRepository extends JpaRepository<CalonMahasiswa, Long> {
    CalonMahasiswa findByNik(String nik);
    CalonMahasiswa findByUser(User user);

   

    @Query("SELECT cm FROM CalonMahasiswa cm " +
           "LEFT JOIN FETCH cm.jurusanDiterima jd " +
           "LEFT JOIN FETCH jd.jurusan j")
    List<CalonMahasiswa> findAllWithJurusanDiterima();
}


