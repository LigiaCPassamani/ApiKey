package com.apikey.apikey.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "auth_api_keys")
public class AuthApiKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiKey;

    private String client;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "AUTH_API_KEY_ROLES",
            joinColumns = {
                    @JoinColumn(name = "AUTH_API_KEYS_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLES_ID")
            })
    private Set<Role> roles;

}
