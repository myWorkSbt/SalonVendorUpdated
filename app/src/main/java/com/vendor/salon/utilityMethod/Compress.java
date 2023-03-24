package com.vendor.salon.utilityMethod;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Compress {
    public static byte[] images(String imagePath, int maxFileSizeInBytes) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int compressQuality = 100; // Start with 100% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, byteArrayOutputStream);
        while (byteArrayOutputStream.toByteArray().length > maxFileSizeInBytes) {
            // Keep compressing the image until the file size is less than or equal to the specified limit
            if (compressQuality > 10 ) {
                compressQuality -= 10;
                byteArrayOutputStream.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, byteArrayOutputStream);
            }
            else {
                break;
            }
        }
        byte[] compressedBytes = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedBytes;
    }
}
