package dev.easycloud;

import dev.easycloud.dependency.DependencyLoader;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.jar.JarFile;

public final class Bootstrap {

    public void load() {
        try {
            var localPath = Paths.get("local");
            var dependenciesPath = Paths.get("dependencies");
            var librariesPath = dependenciesPath.resolve("libraries");

            // Get the path to the current running jar file
            var bootstrapFile = new JarFile(
                    Paths.get(
                            Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                    ).toFile()
            );

            // Create directories if they do not exist
            if (Files.notExists(localPath)) {
                Files.createDirectories(localPath);
            }
            if (Files.notExists(dependenciesPath)) {
                Files.createDirectories(dependenciesPath);
            }
            if (Files.notExists(librariesPath)) {
                Files.createDirectories(librariesPath);
            }

            // Delete the "dynamic" directory recursively if it exists
            var dynamicPath = localPath.resolve("dynamic");
            if (Files.exists(dynamicPath)) {
                deleteRecursively(dynamicPath);
            }

            // Copy resource jar files to the libraries directory
            copyFile("grpc.jar", librariesPath.resolve("grpc.jar"));
            copyFile("cluster.jar", librariesPath.resolve("cluster.jar"));

            // Clear the console using ANSI escape codes
            System.out.print("\u001b[H\u001b[2J");
            System.out.flush();

            // Print info log with timestamp
            System.out.printf("[\u001B[97m%s\u001B[0m] \u001B[97mINFO\u001B[0m: Loading dependencies...%n", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            // Load dependencies from the dependencies directory
            new DependencyLoader().load(dependenciesPath);

            System.out.print("\u001b[H\u001b[2J");
            System.out.flush();

            // Retrieve the project version from the JAR manifest and set it as a system property
            String version = bootstrapFile.getManifest().getMainAttributes().getValue("project-version");
            if (version == null) {
                version = "unknown";
            }
            System.setProperty("version", version);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            // Build classpath argument, adjusting separators depending on the OS
            var fileArg = "dependencies/libraries/cluster.jar;dependencies/*;";
            if (!System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win")) {
                fileArg = fileArg.replace(";", ":");
            }

            // Start a new Java process with the specified classpath and system properties
            var process = new ProcessBuilder(
                    "java",
                    "-cp", fileArg,
                    "-Dfile.encoding=UTF-8",
                    "-Dversion=" + System.getProperty("version"),
                    "dev.easycloud.ClusterBootKt"
            )
                    .inheritIO() // Inherit IO streams for output
                    .start();

            // Wait for the process to finish
            process.waitFor();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // Restore interrupted status and exit if the thread is interrupted
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    // Copies a resource from the classpath to the specified destination path
    private void copyFile(String resourceName, Path destination) throws IOException {
        try (var inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource " + resourceName + " not found");
            }
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // Recursively deletes a directory or file
    private void deleteRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (var entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteRecursively(entry);
                }
            }
        }
        Files.delete(path);
    }
}
