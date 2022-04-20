package git.crystal.engine.render.util;

import git.crystal.engine.GameObject;
import git.crystal.engine.render.ui.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Handles the Matrices we need to simulate movement or help create a Camera.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/19/2022
 */

public class Transformation {
    private final Matrix4f m_ProjectionMatrix;
    private final Matrix4f m_ModelViewMatrix;
    private  final Matrix4f m_ViewMatrix;

    public Transformation() {
        m_ProjectionMatrix = new Matrix4f();
        m_ModelViewMatrix = new Matrix4f();
        m_ViewMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix(float fov, float zNear, float zFar) {
        return m_ProjectionMatrix.setPerspective(fov, calcAspectRatio(), zNear, zFar);
    }

    public Matrix4f getModelViewMatrix(GameObject object, Matrix4f viewMatrix) {
        Vector3f rotation = object.getRotation();
        m_ModelViewMatrix.identity();
        m_ModelViewMatrix.translate(object.getPosition())
                .rotateX((float) Math.toRadians(-rotation.x()))
                .rotateY((float) Math.toRadians(-rotation.y()))
                .rotateZ((float) Math.toRadians(-rotation.z()))
                .scale(object.getScale());

        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(m_ModelViewMatrix);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f cameraRot = camera.getRotation();

        m_ViewMatrix.identity();
        m_ViewMatrix.rotate((float) Math.toRadians(cameraRot.x()), new Vector3f(1, 0, 0))
                    .rotate((float) Math.toRadians(cameraRot.y()), new Vector3f(0, 1, 0));
                    //.rotate((float) Math.toRadians(cameraRot.z()), new Vector3f(0, 0, 1));
        m_ViewMatrix.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

        return m_ViewMatrix;
    }

    public float getAspectRatio() {
        return calcAspectRatio();
    }

    private float calcAspectRatio() {
        float width, height;

        try(MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            glfwGetWindowSize(Window.Instance().glfwWindow(), w, h);
            width = (float) w.get(0);
            height = (float) h.get(0);
        }

        return width / height;
    }

}
