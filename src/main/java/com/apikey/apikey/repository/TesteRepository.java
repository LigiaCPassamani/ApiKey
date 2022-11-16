package com.apikey.apikey.repository;

import com.apikey.apikey.model.ModelTeste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TesteRepository extends JpaRepository<ModelTeste, Long> {
}
