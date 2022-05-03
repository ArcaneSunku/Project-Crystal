package git.crystal.engine.render;

import org.apache.commons.io.IOUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Handles all the data required to render an image or Texture with OpenGL.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/17/2022
 */

public class Texture {

    private final int m_Id;
    private int m_width, m_height;

    public Texture() {
        m_Id = glGenTextures();
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, m_Id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDeleteTextures(m_Id);
    }

    public void setParameter(int name, int value) {
        glTexParameteri(GL_TEXTURE_2D, name, value);
    }

    public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data, boolean generateMipmaps) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);

        if(generateMipmaps)
            glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void uploadData(int width, int height, ByteBuffer data) {
        uploadData(GL_RGBA8, width, height, GL_RGBA, data, true);
    }

    public static Texture createTexture(int width, int height, ByteBuffer data) {
        Texture result = new Texture();

        result.setWidth(width);
        result.setHeight(height);

        result.bind();

        result.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        result.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        result.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        result.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        result.uploadData(GL_RGBA8, width, height, GL_RGBA, data, true);

        result.unbind();
        return result;
    }

    public static Texture loadInternalTexture(String filePath) {
        ByteBuffer image, buffer = null;
        int width, height;

        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            byte[] imageData = IOUtils.toByteArray(Objects.requireNonNull(Texture.class.getResourceAsStream(filePath)));
            buffer = MemoryUtil.memAlloc(imageData.length);
            buffer.put(imageData);
            buffer.flip();

            stbi_set_flip_vertically_on_load(true);
            image = stbi_load_from_memory(buffer, w, h, comp, 4);
            if(image == null)
                throw new RuntimeException(String.format("Failed to load image [%s]!%n%s", filePath, stbi_failure_reason()));

            width = w.get(0);
            height = h.get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(buffer != null)
                MemoryUtil.memFree(buffer);
        }

        return createTexture(width, height, image);
    }

    public static Texture loadTexture(String filePath) {
        ByteBuffer image;
        int width, height;

        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            image = stbi_load(filePath, w, h, comp, 4);
            if(image == null)
                throw new RuntimeException(String.format("Failed to load image [%s]!%n%s", filePath, stbi_failure_reason()));

            width = w.get(0);
            height = h.get(0);
        }

        return createTexture(width, height, image);
    }

    public void setWidth(int width) {
        if(width > 0)
            m_width = width;
    }

    public void setHeight(int height) {
        if(height > 0)
            m_height = height;
    }

    public int getWidth() {
        return m_width;
    }

    public int getHeight() {
        return m_height;
    }

    public int getId() {
        return m_Id;
    }

}
