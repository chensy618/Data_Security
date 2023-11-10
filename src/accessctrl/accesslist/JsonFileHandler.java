package src.accessctrl.accesslist;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileHandler {

    public static void main(String[] args) throws DatabindException
    {
        AccessCtrlList accessCtrlList = new AccessCtrlList("John", "print", "ordinary_user");


        writeToJsonFile(".//aclist_policy.json", accessCtrlList);

        AccessCtrlList readAccess = readFromJsonFile(".//aclist_policy.json", AccessCtrlList.class);
        if (readAccess != null) {
            System.out.println("Read person from file: " + readAccess);
        }
    }
    // 写入JSON文件
    public static void writeToJsonFile(String filePath, Object data) {
        try {
            // 创建ObjectMapper对象
            ObjectMapper objectMapper = new ObjectMapper();

            // 将对象写入JSON文件
            try
            {
                objectMapper.writeValue(new File(filePath), data);
            }
            catch (DatabindException e)
            {
                throw new RuntimeException(e);
            }

            System.out.println("Data has been written to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从JSON文件读取数据
    public static <T> T readFromJsonFile(String filePath, Class<T> valueType) throws DatabindException
    {
        try {
            // 创建ObjectMapper对象
            ObjectMapper objectMapper = new ObjectMapper();

            // 从JSON文件读取数据
            T data = objectMapper.readValue(new File(filePath), valueType);

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
