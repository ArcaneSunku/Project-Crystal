package git.crystal.engine.input;

import git.crystal.engine.render.ui.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles Mouse related things that have to do with input.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 5/1/2022
 */

public class MouseInput {

    private static MouseInput s_Instance = null;
    public static MouseInput Instance() {
        if(s_Instance == null)
            s_Instance = new MouseInput();

        return s_Instance;
    }

    private final Vector2d m_PreviousPos, m_CurrentPos;
    private final Vector2f m_DisplayVec;

    private boolean m_inWindow;
    private boolean m_leftPressed, m_rightPressed;

    private MouseInput() {
        m_PreviousPos = new Vector2d(-1.0, -1.0);
        m_CurrentPos = new Vector2d();
        m_DisplayVec = new Vector2f();

        m_inWindow = false;
        m_leftPressed = false;
        m_rightPressed = false;
    }

    public void init() {
        glfwSetCursorPosCallback(Window.Instance().glfwWindow(), (window, xPos, yPos) -> {
            m_CurrentPos.x = xPos;
            m_CurrentPos.y = yPos;
        });

        glfwSetCursorEnterCallback(Window.Instance().glfwWindow(), (window, entered) -> {
            m_inWindow = entered;
        });

        glfwSetMouseButtonCallback(Window.Instance().glfwWindow(), (window, button, action, mode) -> {
            m_leftPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            m_rightPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public void update() {
        m_DisplayVec.x = 0;
        m_DisplayVec.y = 0;

        if(m_PreviousPos.x > 0 && m_PreviousPos.y > 0 && m_inWindow) {
            double deltaX = m_CurrentPos.x - m_PreviousPos.x;
            double deltaY = m_CurrentPos.y - m_PreviousPos.y;

            boolean rotateX = deltaX != 0;
            if(rotateX)
                m_DisplayVec.y = (float) deltaX;

            boolean rotateY = deltaY != 0;
            if(rotateY)
                m_DisplayVec.x = (float) deltaY;
        }

        m_PreviousPos.x = m_CurrentPos.x;
        m_PreviousPos.y = m_CurrentPos.y;
    }

    public static  Vector2f getDisplayVec() {
        return Instance().m_DisplayVec;
    }

    public static boolean isInWindow() {
        return Instance().m_inWindow;
    }

    public static  boolean isLeftPressed() {
        return Instance().m_leftPressed;
    }

    public static  boolean isRightPressed() {
        return Instance().m_rightPressed;
    }

}
