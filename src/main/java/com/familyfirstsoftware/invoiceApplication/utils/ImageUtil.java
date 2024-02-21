package com.familyfirstsoftware.invoiceApplication.utils;

import com.familyfirstsoftware.invoiceApplication.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
@Slf4j
public class ImageUtil {
    private static final long MAX_FILE_SIZE = 10485760; // 10 MB
    private static final List<String> ACCEPTABLE_FILE_FORMATS = Arrays.asList("image/jpeg", "image/png", "image/gif");

    public static void saveImage(String email, MultipartFile image) {
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/images").toAbsolutePath().normalize();
        try {
            if(!Files.exists(fileStorageLocation)) { // create the directory if it doesn't exist
                Files.createDirectories(fileStorageLocation);
                log.info("Directory created at: {}", fileStorageLocation);
            }
            if(!fileStorageLocation.toFile().canWrite()) {
                throw new ApiException("Cannot write to the specified directory. Please check the directory permissions.");
            }
            if(!ACCEPTABLE_FILE_FORMATS.contains(image.getContentType())) {
                throw new ApiException("Invalid file format. Only JPEG, PNG, and GIF are supported.");
            }
            if(image.getSize() > MAX_FILE_SIZE) {
                throw new MaxUploadSizeExceededException(MAX_FILE_SIZE);
            }
            // Hash the image
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(image.getBytes());
            String imageHash = String.format("%064x", new BigInteger(1, hash));

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(email + ".png"), REPLACE_EXISTING);
            log.info("Image saved successfully at: {}", fileStorageLocation.resolve(email + ".png").toAbsolutePath().toString());
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new ApiException("Could not save image. Error: " + exception.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String setUserImageUrl(String email) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(email.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash) +    ".png";
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateImageUrl(String hashedEmail) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/" + hashedEmail + ".png").toUriString();
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}