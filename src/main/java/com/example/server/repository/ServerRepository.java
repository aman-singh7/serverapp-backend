package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.model.Server;

public interface ServerRepository extends JpaRepository<Server, Long> {
    Server findByIpAddress(String ipAddress);
}
