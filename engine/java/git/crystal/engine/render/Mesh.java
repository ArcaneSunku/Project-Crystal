package git.crystal.engine.render;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class will handle all the data related to rendering various shapes with different textures.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/19/2022
 */

public class Mesh {

    // The VAO of our Mesh and it's Data
    private final int m_VAOId;
    // The VBOs to buffer what kind of data the VAO holds
    private final int m_VertVBO, m_TextureVBO, m_IndVBO;

    // How many vertices this mesh has
    private final int m_VertexCount;

    private Texture m_texture;
    private boolean m_useTexture;

    public Mesh(float[] vertices, float[] textCoords, int[] indices, Texture texture) {
        m_VertexCount = indices.length;
        m_texture = texture;

        FloatBuffer verticesBuffer = null;
        FloatBuffer textureBuffer = null;
        IntBuffer indicesBuffer = null;

        if(texture == null)
            m_useTexture = false;
        else
            m_useTexture = true;

        try {
            m_VAOId = glGenVertexArrays();
            glBindVertexArray(m_VAOId);

            // Position VBO
            m_VertVBO = glGenBuffers();
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            glBindBuffer(GL_ARRAY_BUFFER, m_VertVBO);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);

            // Texture Coordinate VBO
            m_TextureVBO = glGenBuffers();
            textureBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textureBuffer.put(textCoords).flip();

            glBindBuffer(GL_ARRAY_BUFFER, m_TextureVBO);
            glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, NULL);

            // Index VBO
            m_IndVBO = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_IndVBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            // Since we're using MemoryUtil it is important we use memFree on these buffers!
            if(verticesBuffer != null)
                MemoryUtil.memFree(verticesBuffer);

            if(textureBuffer != null)
                MemoryUtil.memFree(textureBuffer);

            if(indicesBuffer != null)
                MemoryUtil.memFree(indicesBuffer);
        }
    }

    public void render() {
        boolean bindTexture = m_texture != null && m_useTexture;
        if(bindTexture) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, m_texture.getId());
        }

        glBindVertexArray(getVAO());

        glDrawElements(GL_TRIANGLES, m_VertexCount, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);

        if(bindTexture) {
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(m_VertVBO);
        glDeleteBuffers(m_IndVBO);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(m_VAOId);
    }

    public void setUseTexture(boolean useTexture) {
        m_useTexture = useTexture;
    }

    public boolean usesTexture() {
        return m_useTexture;
    }

    public int getVAO() {
        return m_VAOId;
    }

    public int getVertexCount() {
        return m_VertexCount;
    }

}
