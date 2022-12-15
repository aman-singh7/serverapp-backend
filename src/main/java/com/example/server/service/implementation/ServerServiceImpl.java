package com.example.server.service.implementation;

import static org.springframework.data.domain.PageRequest.of;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.server.enums.Status;
import com.example.server.model.Server;
import com.example.server.repository.ServerRepository;
import com.example.server.service.ServerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pining server IP: {}", ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        Boolean isReachable = address.isReachable(1000);
        log.info("IP Address: {} is {}", ipAddress, isReachable);
        Server server = serverRepository.findByIpAddress(ipAddress);
        server.setStatus(isReachable ? Status.SERVER_UP : Status.SERVER_DOWN);

        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepository.findAll(of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepository.deleteById(id);
        return true;
    }

    private String setServerImageUrl() {
        String imageName = "server1.png";

        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageName).toUriString();
    }
}
