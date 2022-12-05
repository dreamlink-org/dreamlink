package dreamlink.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.json.JSONObject;

public class FileFns {

    public static void extractZip(File zipFile, File outputDir) {
        try (
            var inputStream = new FileInputStream(zipFile);
            var zipStream = new ZipInputStream(inputStream);
        ) {
            while (true) {
                var entry = zipStream.getNextEntry();
                if (entry == null) {
                    break; // Exit loop if there are no more entries
                }

                var entryFile = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    // Ensure parent directories exist
                    entryFile.getParentFile().mkdirs();
                    try (var outputStream = new FileOutputStream(entryFile)) {
                        zipStream.transferTo(outputStream);
                    }
                }
                zipStream.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void compressIntoZip(File srcDir, File zipFile) {
        try (
            var outputStream = new FileOutputStream(zipFile);
            var zipStream = new ZipOutputStream(outputStream);
        ) {
            var basePath = srcDir.toPath();
            Files.walkFileTree(basePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    var zipEntry = new ZipEntry(basePath.relativize(file).toString());
                    zipStream.putNextEntry(zipEntry);
                    Files.copy(file, zipStream);
                    zipStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    var zipEntry = new ZipEntry(String.format("%s/", basePath.relativize(dir)));
                    zipStream.putNextEntry(zipEntry);
                    zipStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readBytesFromFile(File file) {
        try(var stream = new FileInputStream(file)) {
            if(file.getName().endsWith(".gz")) {
                try(var gzipStream = new GZIPInputStream(stream)) {
                    return gzipStream.readAllBytes();
                }
            } else {
                return stream.readAllBytes();
            }
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
    }

    public static void writeBytesToFile(File file, byte[] bytes) {
        try(var stream = new FileOutputStream(file)) {
            if(file.getName().endsWith(".gz")) {
                try(var gzipStream = new GZIPOutputStream(stream)) {
                    gzipStream.write(bytes);
                    return;
                }
            }
            stream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readStringFromFile(File file) {
        var bytes = readBytesFromFile(file);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeStringToFile(File file, String string) {
        var bytes = string.getBytes(StandardCharsets.UTF_8);
        writeBytesToFile(file, bytes);
    }

    public static JSONObject readJSONFromFile(File file) {
        var string = readStringFromFile(file);
        return new JSONObject(string);
    }

    public static void writeJSONToFile(File file, JSONObject json) {
        var string = json.toString(4);
        writeStringToFile(file, string);
    }

    public static void touchFile(File file) {
        try {
            var ts = System.currentTimeMillis();
            if(!file.exists()) {
                new FileOutputStream(file).close();
            }
            file.setLastModified(ts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteDirectory(File directory) {
        if(directory.exists()) {
            for(var file : directory.listFiles()) {
                if(Files.isSymbolicLink(file.toPath())) {
                    file.delete();
                } else if(file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
            directory.delete();
        }
    }

    public static void copyDirectory(File sourceDirectory, File targetDirectory) {
        if(!sourceDirectory.exists()) {
            return;
        }
        if(!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        for(var file : sourceDirectory.listFiles()) {
            var targetFile = new File(targetDirectory, file.getName());
            if(file.isDirectory()) {
                copyDirectory(file, targetFile);
            } else {
                try(var sourceStream = new FileInputStream(file);
                    var targetStream = new FileOutputStream(targetFile);
                ) {
                    sourceStream.transferTo(targetStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static Path sanitizeUserPath(String arg) {
        if(arg.startsWith("~")) {
            arg = arg.replaceFirst("~", System.getProperty("user.home"));
        }

        var path = Paths.get(arg);
        if(!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(path);
        }

        return path;
    }
    
}
