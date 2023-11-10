package src.accessctrl;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
            System.out.println("File encryption and decryption completed.");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static SecretKey generateSecretKey() throws Exception
    {
        // Generate a 256-bit AES key
        KeyGenerator aesKey = KeyGenerator.getInstance("AES");
        aesKey.init(256, new SecureRandom());
        return aesKey.generateKey();
    }

    public static void encryptFile(String inputFile, String outputFile, Key key) throws Exception
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


    }

    public static void decryptFile(String inputFile, String outputFile, Key key) throws Exception
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

    }

    public static void saveKeyToFile(SecretKey key, String fileName)
    {
        try
        {
            // 将加密后的密钥字节数组保存到文件
            Path filePath = Paths.get(fileName);
            Files.write(filePath, key.getEncoded());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static SecretKey readKeyFromFile(String fileName)
    {
        try
        {
            // 从文件中读取密钥的字节数组
            byte[] encryptedKeyBytes = Files.readAllBytes(Paths.get(fileName));

            // 根据解密后的密钥字节数组恢复密钥对象
            return new SecretKeySpec(encryptedKeyBytes, "AES");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
