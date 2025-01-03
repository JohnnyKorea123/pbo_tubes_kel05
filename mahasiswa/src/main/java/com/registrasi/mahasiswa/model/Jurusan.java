package com.registrasi.mahasiswa.model;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 5, message = "Nama jurusan harus memiliki minimal 5 huruf")
    private String namaJurusan;

    @ManyToMany(mappedBy = "jurusanYangDiminati")
    private List<CalonMahasiswa> calonMahasiswaList;

    @Min(value = 0, message = "Syarat nilai harus lebih dari 0")
    private int SyaratNilai;


   @OneToMany(mappedBy = "jurusan", fetch = FetchType.LAZY)
    private List<JurusanDiterima> jurusanDiterimaList;
}
