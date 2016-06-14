package hr.fer.zemris.util;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mmatak on 6/14/16.
 */
public class FileUtil {
    public static void saveFile(Path fileName, List<String> lines) {
        StringBuilder buff = new StringBuilder();
        for (String line : lines) {
            buff.append(line).append("\n");
        }
        byte[] data = buff.toString().getBytes(StandardCharsets.UTF_8);
        try {
            Files.write(fileName, data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File not saved!",
                    "Error occurred while saving. File is not saved.", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static List<String> load(String fileName) {
        byte[] data;
        Path filePath = Paths.get(fileName);
        try {
            data = Files.readAllBytes(filePath);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error while reading",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new ArrayList<>(Arrays.asList(new String(data, StandardCharsets.UTF_8).split("\\n")));
    }
}
