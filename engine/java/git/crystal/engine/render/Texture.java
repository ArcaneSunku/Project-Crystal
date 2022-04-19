package git.crystal.engine.render;

/**
 * Handles all the data required to render an image or Texture with OpenGL.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/17/2022
 */

public class Texture {

    private final String m_FilePath;

    private int m_textureId;

    public Texture(String filePath) {
        m_FilePath = filePath;
    }

    /**
     * Creates and initializes everything we need to use our Texture with OpenGL.
     * This will handle filters and other various specifics.
     *
     * @param minFilter the OpenGL relevant variable for the min filter
     * @param magFilter the OpenGL relevant variable for the mag filter
     */
    public void createTexture(int minFilter, int magFilter) {

    }

    public int getId() {
        return m_textureId;
    }

}
