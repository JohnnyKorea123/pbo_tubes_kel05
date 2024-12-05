package com.registrasi.mahasiswa.dto;

import com.registrasi.mahasiswa.model.Jurusan;
import com.registrasi.mahasiswa.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalonMahasiswaDTO {
    private Long id;

    @NotBlank(message = "NIK tidak boleh kosong.")
    private String nik;

    @NotBlank(message = "Nama tidak boleh kosong.")
    private String nama;
    
    @NotBlank(message = "notelp tidak boleh kosong.")
    private String notelp;

    @NotEmpty(message = "Jurusan yang diminati tidak boleh kosong.")
    private String jurusanYangDiminati;

    private String statusPenerimaan;

    private Jurusan jurusan;

    private User user;

    private Long hasilTesId;

    // Tambahkan field totalNilai
    private int totalNilai;

    // Getters and Setters
}
