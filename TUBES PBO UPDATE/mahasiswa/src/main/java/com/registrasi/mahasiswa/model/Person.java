package com.registrasi.mahasiswa.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Person {
    private String nik;
    private String nama;
    private String notelp;
}
