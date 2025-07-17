package dev.easycloud.dependency;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class DependencyLoader {

    public void load(Path dependenciesPath) {
        try (var dependenciesStream = ClassLoader.getSystemClassLoader().getResourceAsStream("cloud.dependencies")) {
            if (dependenciesStream == null) {
                throw new IllegalStateException("Resource cloud.dependencies not found");
            }

            // Read the entire dependencies file content as a string
            var content = new String(dependenciesStream.readAllBytes(), StandardCharsets.UTF_8);

            List<String> requiredDependencies = new ArrayList<>();

            // Process each non-empty line in the dependencies file
            for (var line : content.split("\\R")) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Split line into 5 parts: ignored, name, version, checksum, url
                var parts = line.split(":");
                if (parts.length != 5) {
                    for (String part : parts) {
                        System.out.println(part);
                    }
                    throw new IllegalArgumentException("Expected 5 parts but found: " + parts.length + ". Invalid format: " + line);
                }

                var name = parts[1];
                var version = parts[2];
                var checksum = parts[3];
                var url = parts[4];

                var fileName = name + "-" + version + ".jar";
                var filePath = dependenciesPath.resolve(fileName);

                // Check if dependency file exists
                if (Files.exists(filePath)) {
                    // Verify checksum
                    var validChecksum = checksum(filePath, checksum);
                    if (!validChecksum) {
                        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        System.out.println(String.format(
                                "[\u001B[97m%s\u001B[0m] \u001B[97mERROR\u001B[0m: Checksum mismatch for \u001B[97m%s\u001B[0m. Expected: \u001B[97m%s\u001B[0m",
                                time, fileName, checksum));
                        Files.delete(filePath);
                    }
                }

                // Download dependency if it does not exist
                if (Files.notExists(filePath)) {
                    var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    System.out.println(String.format(
                            "[\u001B[97m%s\u001B[0m] \u001B[97mINFO\u001B[0m: Downloading \u001B[97m%s\u001B[0m...",
                            time, fileName));
                    try (var in = new URL("https://" + url).openStream()) {
                        Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                requiredDependencies.add(fileName);
            }

            // Remove files in the dependencies directory that are not required
            var files = dependenciesPath.toFile().listFiles();
            if (files != null) {
                for (var file : files) {
                    if (file.isDirectory()) continue;
                    if (!requiredDependencies.contains(file.getName())) {
                        var time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        System.out.println(String.format(
                                "[\u001B[97m%s\u001B[0m] \u001B[97mINFO\u001B[0m: Removing unused dependency \u001B[97m%s\u001B[0m...",
                                time, file.getName()));
                        Files.deleteIfExists(file.toPath());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checksum(Path dependency, String expectedChecksum) {
        try (var stream = Files.newInputStream(dependency)) {
            var digest = MessageDigest.getInstance("SHA-256");
            var buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hash = digest.digest();

            // Convert byte array to hex string
            var hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString().equals(expectedChecksum);
        } catch (Exception e) {
            throw new RuntimeException("Error computing checksum: " + e.getMessage(), e);
        }
    }
}
