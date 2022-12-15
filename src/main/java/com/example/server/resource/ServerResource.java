package com.example.server.resource;

import static com.example.server.enums.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.example.server.model.Response;
import com.example.server.model.Server;
import com.example.server.service.implementation.ServerServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
        private final ServerServiceImpl serverService;

        @GetMapping("/list")
        public ResponseEntity<Response> getServers() {
                return ResponseEntity.ok(
                                Response.builder()
                                                .timeStamp(now())
                                                .data(of("servers", serverService.list(10)))
                                                .message("Servers retreived")
                                                .status(OK)
                                                .statusCode(OK.value())
                                                .build());
        }

        @GetMapping("/ping/{ipAddress}")
        public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
                Server server = serverService.ping(ipAddress);
                return ResponseEntity.ok(
                                Response.builder()
                                                .timeStamp(now())
                                                .data(of("server", server))
                                                .message(server.getStatus() == SERVER_UP ? "Ping success"
                                                                : "Ping failed")
                                                .status(OK)
                                                .statusCode(OK.value())
                                                .build());
        }

        @PostMapping("/save")
        public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
                return ResponseEntity.ok(
                                Response.builder()
                                                .timeStamp(now())
                                                .data(of("server", serverService.create(server)))
                                                .message("Server added")
                                                .status(CREATED)
                                                .statusCode(CREATED.value())
                                                .build());
        }

        @GetMapping("/get/{id}")
        public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
                return ResponseEntity.ok(
                                Response.builder()
                                                .timeStamp(now())
                                                .data(of("server", serverService.get(id)))
                                                .message("Server retrieved")
                                                .status(OK)
                                                .statusCode(OK.value())
                                                .build());
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
                return ResponseEntity.ok(
                                Response.builder()
                                                .timeStamp(now())
                                                .data(of("deleted", serverService.delete(id)))
                                                .message("Server deleted")
                                                .status(OK)
                                                .statusCode(OK.value())
                                                .build());
        }

        @GetMapping(path = "/image/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
        public byte[] loadImage(@PathVariable("fileName") String fileName) throws IOException {
                var imageFile = new ClassPathResource("static/image/" + fileName).getFile();
                return Files.readAllBytes(Path.of(imageFile.getAbsolutePath()));
        }
}
