package git.crystal.engine.gfx;

import git.crystal.engine.utils.Files;

import static org.lwjgl.opengl.GL11C.GL_FALSE;
import static org.lwjgl.opengl.GL20C.*;

/**
 * Represents an instance of a Shader Program.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/17/2022
 */

public class Shader {

    private final String m_VertexFile, m_FragmentFile;

    private int m_programId;

    public Shader(String vertexFile, String fragmentFile) {
        m_VertexFile = vertexFile;
        m_FragmentFile = fragmentFile;
    }

    public void create() {
        m_programId = glCreateProgram();
        if(m_programId == GL_FALSE)
            throw new RuntimeException("Failed to create a Shader Program!");

        int vertShader = createShader(Files.readFileToString(m_VertexFile), GL_VERTEX_SHADER);
        int fragmentShader = createShader(Files.readFileToString(m_FragmentFile), GL_FRAGMENT_SHADER);

        glLinkProgram(m_programId);
        if (glGetProgrami(m_programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(m_programId, 1024));
        }

        if (vertShader != 0) {
            glDetachShader(m_programId, vertShader);
        }
        if (fragmentShader != 0) {
            glDetachShader(m_programId, fragmentShader);
        }

        glValidateProgram(m_programId);
        if (glGetProgrami(m_programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(m_programId, 1024));
        }

        glDeleteShader(vertShader);
        glDeleteShader(fragmentShader);
    }

    public void bind() {
        glUseProgram(m_programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();

        if(m_programId != 0)
            glDeleteProgram(m_programId);
    }

    private int createShader(String shaderSource, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if(shaderId == GL_FALSE)
            throw new RuntimeException(String.format("Failed to create a %s Shader!", shaderType == GL_VERTEX_SHADER ? "Vertex" : "Fragment"));

        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));

        glAttachShader(m_programId, shaderId);
        return shaderId;
    }

}
