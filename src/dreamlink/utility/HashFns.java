package dreamlink.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFns {

    public static String generateHash(byte[] bytes) {
        try {
            var messageDigest = MessageDigest.getInstance("SHA-256");
            var builder = new StringBuilder();
            messageDigest.update(bytes);
            var bytesDigest = messageDigest.digest();
            for(var b : bytesDigest) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
}
