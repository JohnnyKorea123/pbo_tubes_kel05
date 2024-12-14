package com.registrasi.mahasiswa.model;





import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class HasilTes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nik;
    private String nama;
    private String jurusanYangDiminati;
    private int nilaiA;
    private int nilaiB;
    private int nilaiC;
    private int totalNilai;

    @OneToOne(mappedBy = "hasilTes")
    private CalonMahasiswa calonMahasiswa;
}
