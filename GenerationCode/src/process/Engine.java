package process;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Engine {

    public static void write(String data, String filePathTarget) throws IOException {
        Path path = Paths.get(filePathTarget);
        byte[] strToBytes = data.getBytes();

        if (!Files.exists(path.getParent()))
            Files.createDirectories(path.getParent());

        Files.write(path, strToBytes);
    }

    public static JsonObject readConfig(String filePath) throws IOException {
        Gson gson = new Gson();
        Path path = Paths.get(filePath);
        List<String> files = Files.readAllLines(path);
        StringBuilder result = new StringBuilder();

        for (String s : files ) {
            result.append(s);
        }

        return gson.fromJson(result.toString(), JsonObject.class);
    }
}
