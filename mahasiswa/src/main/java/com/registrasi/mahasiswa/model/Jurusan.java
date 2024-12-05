package com.registrasi.mahasiswa.model;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Jurusan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "namaJurusan tidak boleh kosong")
    private String namaJurusan;

    @OneToMany(mappedBy = "jurusan")
    private List<CalonMahasiswa> calonMahasiswaList;

    // Getters and Setters
}
