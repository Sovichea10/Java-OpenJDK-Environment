package com.camcyber.shares;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReceiptNumberGenerator {

    public static String generateReceiptNumber() {
        // Get current date and time
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        // Generate a unique identifier (UUID)
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // Combine date/time and UUID to create a receipt number
        return dateFormat.format(now) + uuid.substring(0, 6);
    }
}