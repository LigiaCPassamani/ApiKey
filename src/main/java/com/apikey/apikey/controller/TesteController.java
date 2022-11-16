package com.apikey.apikey.controller;

import com.apikey.apikey.model.ModelTeste;
import com.apikey.apikey.repository.TesteRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/teste")
public class TesteController {

    private final TesteRepository testeRepository;

    public TesteController(TesteRepository testeRepository) {
        this.testeRepository = testeRepository;
    }

    @PreAuthorize("hasAnyRole('TESTE', 'ADMIN')")
    @GetMapping("/find/all")
    public List<ModelTeste> findAll() {
        return testeRepository.findAll();
    }


}
