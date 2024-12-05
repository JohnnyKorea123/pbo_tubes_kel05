package com.registrasi.mahasiswa.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HasilTesDTO {
    private Long id;
    private String nik;
    private String nama;
    private String jurusanYangDiminati;
    private int nilaiA;
    private int nilaiB;
    private int nilaiC;
    private int totalNilai;
    private String statusPenerimaan;

    // Getters and Setters
}
