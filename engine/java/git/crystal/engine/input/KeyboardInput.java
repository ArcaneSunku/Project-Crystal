package git.crystal.engine.input;

import git.crystal.engine.render.ui.Window;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Replace with description for KeyboardInput
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 5/1/2022
 */

public class KeyboardInput {

    private static KeyboardInput s_Instance = null;
    public static KeyboardInput Instance() {
        if(s_Instance == null)
            s_Instance = new KeyboardInput();

        return s_Instance;
    }

    private final HashMap<Integer, Boolean> m_Keys;

    private KeyboardInput() {
        m_Keys = new LinkedHashMap<>();

        for(int i = 32; i < GLFW_KEY_LAST; i++)
            m_Keys.put(i, false);
    }

    public void init() {
        glfwSetKeyCallback(Window.Instance().glfwWindow(), (window, key, scancode, action, mod) -> {
            m_Keys.put(key, action != GLFW_RELEASE);
        });
    }

    public static boolean isKeyPressed(int key) {
        return Instance().m_Keys.get(key);
    }

}
