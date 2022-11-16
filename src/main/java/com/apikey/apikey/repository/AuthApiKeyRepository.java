package com.apikey.apikey.repository;

import com.apikey.apikey.model.AuthApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthApiKeyRepository extends JpaRepository<AuthApiKey, Long> {

    AuthApiKey findByApiKey(String apiKey);
}
