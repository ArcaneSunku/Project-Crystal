package git.crystal.engine.render;

import git.crystal.engine.GameObject;
import git.crystal.engine.render.util.Camera;
import git.crystal.engine.render.util.Transformation;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;

/**
 * This class handles everything that has to do with Rendering our data to the screen.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/19/2022
 */

public class Renderer {

    public static final float FOV = (float) Math.toRadians(60.0f);
    public static final float Z_NEAR = 0.01f, Z_FAR = 1000.0f;

    private final Transformation m_Transformation;

    private Shader m_shader;

    public Renderer(Shader shader) {
        m_shader = shader;

        m_Transformation = new Transformation();
    }

    /**
     * Call this before using any other Renderer method! This initializes everything needed for the
     * class to do everything that it needs. From the shaders to anything else we might need.
     */
    public void initialize() {
        m_shader.create();

        m_shader.createUniform("uProjectionMatrix");
        m_shader.createUniform("uModelViewMatrix");

        m_shader.createUniform("uTextureSampler");
        m_shader.createUniform("uUseTexture");
    }

    /**
     * Renders every GameObject in the List that is passed into it. For now, we do nothing about how often we
     * are rendering things nor are we considering if they're even in frame.
     *
     * @param gameObjects a List of GameObjects to be processed and rendered
     * @param camera the Camera we need to display our simulation/game to the screen
     */
    public void draw(List<GameObject> gameObjects, Camera camera) {
        m_shader.bind();

        Matrix4f projectionMatrix = m_Transformation.getProjectionMatrix(FOV, Z_NEAR, Z_FAR);
        m_shader.setUniform("uProjectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = m_Transformation.getViewMatrix(camera);
        m_shader.setUniform("uTextureSampler", 0);

        for(GameObject obj : gameObjects) {
            Mesh mesh = obj.getMesh();
            Matrix4f modelViewMatrix = m_Transformation.getModelViewMatrix(obj, viewMatrix);
            m_shader.setUniform("uModelViewMatrix", modelViewMatrix);

            m_shader.setUniform("uUseTexture", mesh.usesTexture() ? GL_TRUE : GL_FALSE);
            mesh.render();
        }

        m_shader.unbind();
    }

    /**
     * Cleans everything up that the Renderer was using. This way all that memory can go when the
     * Java Garbage Collector decides it wants to take it.
     */
    public void cleanup() {
        if(m_shader != null)
            m_shader.cleanup();
    }

    public void setShader(Shader shader) {
        if(m_shader != null)
            m_shader.cleanup();

        m_shader = shader;
    }

    public Shader getShader() {
        return m_shader;
    }

}
