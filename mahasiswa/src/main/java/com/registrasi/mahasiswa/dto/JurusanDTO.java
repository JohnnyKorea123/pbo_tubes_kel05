package com.registrasi.mahasiswa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JurusanDTO {
    private Long id;

    @NotBlank(message = "Nama jurusan tidak boleh kosong")
    @Size(min = 10, message = "Nama jurusan harus memiliki minimal 10 huruf")
    private String namaJurusan;
    
    // Getters and Setters
}
