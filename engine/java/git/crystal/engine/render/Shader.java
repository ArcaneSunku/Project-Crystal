package git.crystal.engine.render;

import git.crystal.engine.utils.Files;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11C.GL_FALSE;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Represents an instance of a Shader Program.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/17/2022
 */

public class Shader {

    private final Map<String, Integer> m_Uniforms;
    private final String m_VertexFile, m_FragmentFile;

    private int m_programId;

    public Shader(String vertexFile, String fragmentFile) {
        m_Uniforms = new HashMap<>();

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
            throw new RuntimeException(String.format("Error linking Shader code: %s", glGetProgramInfoLog(m_programId, 1024)));
        }

        if (vertShader != 0) {
            glDetachShader(m_programId, vertShader);
            glDeleteShader(vertShader);
        }
        if (fragmentShader != 0) {
            glDetachShader(m_programId, fragmentShader);
            glDeleteShader(fragmentShader);
        }

        glValidateProgram(m_programId);
        if (glGetProgrami(m_programId, GL_VALIDATE_STATUS) == GL_FALSE)
            System.err.printf("Warning validating Shader code: %s%n", glGetProgramInfoLog(m_programId, 1024));
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

    public void createUniform(String uniformName) {
        int location = glGetUniformLocation(m_programId, uniformName);
        if(location < 0) {
            System.err.printf("Could not find uniform: [%s]%n", uniformName);
            return;
        } else if(m_Uniforms.containsKey(uniformName)) {
            System.err.printf("Uniform [%s] already exists!%n", uniformName);
            return;
        }

        m_Uniforms.put(uniformName, location);
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(getUniformLocation(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        glUniform1f(getUniformLocation(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        setUniform(uniformName, value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, float x, float y, float z) {
        glUniform3f(getUniformLocation(uniformName), x, y, z);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(getUniformLocation(uniformName), false, buffer);
        }
    }

    private int getUniformLocation(String uniformName) {
        return m_Uniforms.get(uniformName);
    }

    /**
     * Creates the shader we want out of the source we pass to it. This handles everything we need
     * to use our Shaders with our Shader Program.
     *
     * @param shaderSource the long String source of the Shader
     * @param shaderType the type of shader we'll create and compile
     * @return the Shader ID we use to interact with the Program
     */
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
