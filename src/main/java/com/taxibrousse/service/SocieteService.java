package com.taxibrousse.service;

import com.taxibrousse.entity.Societe;
import com.taxibrousse.repository.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocieteService {

    @Autowired
    private SocieteRepository societeRepository;

    public List<Societe> findAll() {
        return societeRepository.findAll();
    }

    public Optional<Societe> findById(Long id) {
        return societeRepository.findById(id);
    }

    public Societe save(Societe societe) {
        return societeRepository.save(societe);
    }

    public void deleteById(Long id) {
        societeRepository.deleteById(id);
    }
}
