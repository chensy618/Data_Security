package src;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.SecureRandom;

public class FileEncryption
{

    public static void main(String[] args)
    {
        try
        {
            // Generate a secret key
            SecretKey secretKey = generateSecretKey();

            // Encrypt the file
            encryptFile(".\\users.txt", ".\\encrypted.txt", secretKey);

            // Decrypt the file
            decryptFile(".\\encrypted.txt", ".\\users.txt", secretKey);

            System.out.println("File encryption and decryption completed.");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static SecretKey generateSecretKey() throws Exception
    {
        // Generate a 256-bit AES key
        KeyGenerator aesKey = KeyGenerator.getInstance("AES");
        aesKey.init(256, new SecureRandom());
        return aesKey.generateKey();
    }

    private static void encryptFile(String inputFile, String outputFile, Key key) throws Exception
    {
        // Create cipher and initialize for encryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Create input and output streams
        FileInputStream inputEncrypt = new FileInputStream(inputFile);
        FileOutputStream outputEncrypt = new FileOutputStream(outputFile);
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputEncrypt, cipher);

        // Encrypt the file
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputEncrypt.read(buffer)) != -1)
        {
            cipherOutputStream.write(buffer, 0, bytesRead);
        }

        // Close streams
        inputEncrypt.close();
        cipherOutputStream.close();

        try
        {
            // Using java.nio.file.Files
            Path path = Paths.get(inputFile);
            Files.delete(path);
            System.out.println("Original file deleted successfully.");

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Failed to delete the original file: " + e.getMessage());
        }

    }

    private static void decryptFile(String inputFile, String outputFile, Key key) throws Exception
    {
        // Create cipher and initialize for decryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Create input and output streams
        FileInputStream inputDecrypt = new FileInputStream(inputFile);
        CipherInputStream cipherinputStream = new CipherInputStream(inputDecrypt, cipher);
        FileOutputStream outputDecrypt = new FileOutputStream(outputFile);

        // Decrypt the file
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = cipherinputStream.read(buffer)) != -1)
        {
            outputDecrypt.write(buffer, 0, bytesRead);
        }

        // Close streams
        cipherinputStream.close();
        outputDecrypt.close();

        try
        {
            // Using java.nio.file.Files
            Path path = Paths.get(inputFile);
            Files.delete(path);
            System.out.println("Encryption File deleted successfully.");

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Failed to delete the encryption file: " + e.getMessage());
        }
    }
}
