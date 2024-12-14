package com.registrasi.mahasiswa.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class CalonMahasiswa extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statusPenerimaan;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "hasil_tes_id")
    private HasilTes hasilTes;

    @ManyToMany
    @JoinTable(
        name = "calon_mahasiswa_jurusan",
        joinColumns = @JoinColumn(name = "calon_mahasiswa_id"),
        inverseJoinColumns = @JoinColumn(name = "jurusan_id")
    )
    private List<Jurusan> jurusanYangDiminati;

    @OneToOne(mappedBy = "calonMahasiswa", fetch = FetchType.EAGER)
    private JurusanDiterima jurusanDiterima;

}





