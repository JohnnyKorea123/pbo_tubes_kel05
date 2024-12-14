package com.registrasi.mahasiswa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registrasi.mahasiswa.model.JurusanDiterima;
import com.registrasi.mahasiswa.repository.JurusanDiterimaRepository;

@Service
public class JurusanDiterimaService {
    @Autowired
    private JurusanDiterimaRepository jurusanDiterimaRepository;

    public JurusanDiterima save(JurusanDiterima jurusanDiterima) {
        return jurusanDiterimaRepository.save(jurusanDiterima);
    }
}
