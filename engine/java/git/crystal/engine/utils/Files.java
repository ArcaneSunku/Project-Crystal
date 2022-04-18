package git.crystal.engine.utils;

import java.io.*;
import java.util.Scanner;

/**
 * Handles everything that has to do with File related operations within our Game Engine.
 * Such as reading from or writing to a file.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/17/2022
 */

public class Files {

    public static String readFileToString(String filePath) {
        return readFileToString(filePath, true);
    }

    public static String readFileToString(String filePath, boolean internal) {
        String result;
        try {
            if(internal) {
                InputStream is = Files.class.getResourceAsStream(filePath);
                if(is == null)
                    throw new IOException(String.format("Failed to load file: [%s]", filePath));

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line = br.readLine();
                do {
                    sb.append(line).append("\n");
                } while((line = br.readLine()) != null);

                br.close();
                is.close();
                result = sb.toString();
            } else {
                File file = new File(filePath);
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();

                String line = br.readLine();
                do {
                    sb.append(line).append("\n");
                } while((line = br.readLine()) != null);

                result = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return "nil";
        }

        return result;
    }

}
