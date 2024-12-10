package com.registrasi.mahasiswa.dto;

import java.util.List;

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
    private List<JurusanDTO> jurusanYangDiminati;

    private String statusPenerimaan;
    private Long hasilTesId;
    private int totalNilai;
}
