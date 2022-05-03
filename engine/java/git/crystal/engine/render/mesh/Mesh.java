package git.crystal.engine.render.mesh;

import git.crystal.engine.render.Texture;
import org.joml.Vector3f;
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
    private final int m_VertVBO, m_TextureVBO, m_NormVBO, m_IndVBO;

    // How many vertices this mesh has
    private final int m_VertexCount;

    private final Vector3f m_Color;

    private Texture m_texture;
    private boolean m_useTexture;

    public Mesh(float[] vertices, float[] textCoords, float[] normals, int[] indices) {
        m_VertexCount = indices.length;
        m_Color = new Vector3f(1f, 1f, 1f);

        FloatBuffer verticesBuffer = null;
        FloatBuffer textureBuffer = null;
        FloatBuffer normalBuffer = null;
        IntBuffer indicesBuffer = null;

        m_useTexture = false;

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

            // Normals VBO
            m_NormVBO = glGenBuffers();
            normalBuffer = MemoryUtil.memAllocFloat(normals.length);
            normalBuffer.put(normals).flip();

            glBindBuffer(GL_ARRAY_BUFFER, m_NormVBO);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, NULL);

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

            if(normalBuffer != null)
                MemoryUtil.memFree(normalBuffer);

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

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, m_VertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

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
        glDeleteBuffers(m_TextureVBO);
        glDeleteBuffers(m_NormVBO);
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

    public void setColor(Vector3f color) {
        setColor(color.x, color.y, color.z);
    }

    public void setColor(float r, float g, float b) {
        m_Color.x = r;
        m_Color.y = g;
        m_Color.z = b;
    }

    public void setTexture(Texture texture) {
        m_texture = texture;
    }

    public Vector3f getColor() {
        return m_Color;
    }

    public Texture getTexture() {
        return m_texture;
    }

    public int getVAO() {
        return m_VAOId;
    }

    public int getVertexCount() {
        return m_VertexCount;
    }

}
