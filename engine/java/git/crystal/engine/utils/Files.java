package git.crystal.engine.utils;

import java.io.*;

/**
 * Handles everything that has to do with File related operations within our Game Engine.
 * Such as basic reading or writing operations on a file.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/17/2022
 */

public class Files {

    /**
     * Reads any text file from within our jar and turns it into a String that we can use in our program.
     *
     * @param filePath the path of the file we want to read into a String
     * @return the contents of the file, converted into a String
     */
    public static String readFileToString(String filePath) {
        return readFileToString(filePath, true);
    }

    /**
     * Reads any text file and turns it into a String we can use in our program.
     *
     * @param filePath the path of the file we want to read into a String
     * @param internal whether you want to read this from within the jar file or not
     * @return the contents of the file, converted into a String
     */
    public static String readFileToString(String filePath, boolean internal) {
        String result;
        BufferedReader br;
        StringBuilder sb;
        try {
            if(internal) {
                InputStream is = Files.class.getResourceAsStream(filePath);
                if(is == null)
                    throw new IOException(String.format("Failed to load file: [%s]", filePath));

                br = new BufferedReader(new InputStreamReader(is));
                sb = new StringBuilder();

                String line = br.readLine();
                do {
                    sb.append(line).append("\n");
                } while((line = br.readLine()) != null);

                is.close();
            } else {
                File file = new File(filePath);
                if(!file.exists())
                    throw new IOException(String.format("Failed to load file: [%s]", filePath));

                br = new BufferedReader(new FileReader(file));
                sb = new StringBuilder();

                String line = br.readLine();
                do {
                    sb.append(line).append("\n");
                } while((line = br.readLine()) != null);
            }

            br.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return "nil";
        }

        return result;
    }

}
