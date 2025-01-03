package com.registrasi.mahasiswa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JurusanDTO {
    private Long id;

    @NotBlank(message = "Nama jurusan tidak boleh kosong")
    @Size(min = 5, message = "Nama jurusan harus memiliki minimal 5 huruf")
    private String namaJurusan;


    @Min(value = 0, message = "Syarat nilai harus lebih dari 0")
    private int SyaratNilai;
    
}
