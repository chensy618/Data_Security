package src.accessctrltask1.accesslist;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JsonFileHandler
{

    public void main(String[] args) throws DatabindException
    {
        //only for test here
    }

    // 写入JSON文件
    public void writeToJsonFile(String filePath, AccessCtrlList accessList)
    {
        try
        {
            // Create ObjectMapper object
            ObjectMapper objectMapper = new ObjectMapper();

            // Enable pretty printing
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Read existing data from the file, if any
            List<Map<String, Object>> existingUsers = readUsersFromFile(filePath);

            // Add the new user to the existing data
            existingUsers.add(Map.of("username", accessList.username, "access", accessList.method));

            // Create a map with a "users" key and the list of users
            Map<String, List<Map<String, Object>>> data = Map.of("users", existingUsers);

            // Write the updated data to the JSON file using try-with-resources
            try (var fileWriter = new FileWriter(filePath))
            {
                objectMapper.writeValue(fileWriter, data);
                //System.out.println("User has been added to " + filePath);
            }
            catch (IOException e)
            {
                throw new RuntimeException("IO error while writing to file: " + e.getMessage(), e);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 从JSON文件读取数据
    public List<Map<String, Object>> readUsersFromFile(String filePath)
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(filePath);

            // If the file doesn't exist or is empty, return a new Arraylist
            // notice : here you could not return an empty list, you must return the data structure.
            if (!file.exists() || file.length() == 0)
            {
                return new ArrayList<>();
            }

            // Read the existing data from the file
            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(file, objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, List.class));

            // Extract the list of users from the "users" key
            return data.getOrDefault("users", new ArrayList<>());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error reading from file: " + e.getMessage(), e);
        }
    }


}
