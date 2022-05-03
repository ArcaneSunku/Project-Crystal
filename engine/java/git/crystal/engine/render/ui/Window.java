package git.crystal.engine.render.ui;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A class to let us create a GLFWwindow and interface with OpenGL.
 * We use a static class called Settings to define how the window will appear.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/16/2022
 */

public class Window {

    private static Window s_Instance = null;
    public static Window Instance() {
        if(s_Instance == null)
            s_Instance = new Window();

        return s_Instance;
    }

    private final Settings m_Settings;

    private long m_glfwWindow;

    private Window() {
        m_Settings = new Settings();
    }

    public boolean create() {
        if(!glfwInit()) {
            System.err.println("FATAL ERROR: GLFW Failed to initialize!");
            return false;
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, m_Settings.resizable ? GLFW_TRUE : GLFW_FALSE);

        m_glfwWindow = glfwCreateWindow(m_Settings.width, m_Settings.height, m_Settings.title, m_Settings.fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if(m_glfwWindow == NULL) {
            System.err.println("Failed to create a Window!");
            return false;
        }

        if(!m_Settings.fullscreen) {
            try (MemoryStack stack = stackPush()) {
                GLFWVidMode vid_mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                if(vid_mode == null) {
                    System.err.println("Failed to find the settings of your monitor!");
                    return false;
                }

                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);

                glfwGetWindowSize(m_glfwWindow, w, h);
                glfwSetWindowPos(m_glfwWindow, (vid_mode.width() - w.get(0)) / 2, (vid_mode.height() - h.get(0)) / 2);
            }
        }

        glfwMakeContextCurrent(m_glfwWindow);
        glfwSwapInterval(m_Settings.useVSync ? m_Settings.vBlanks : 0);
        GL.createCapabilities();

        glfwSetWindowSizeCallback(m_glfwWindow, ((window, width, height) -> {
            Instance().getSettings().width = width;
            Instance().getSettings().height = height;
        }));

        glfwSetFramebufferSizeCallback(m_glfwWindow, (((window, width, height) -> {
            glViewport(0, 0, width, height);
        })));

        return true;
    }

    public void display() {
        if(m_glfwWindow != NULL)
            glfwShowWindow(m_glfwWindow);
    }

    public void update() {
        glfwPollEvents();

        if(m_glfwWindow != NULL)
            glfwSwapBuffers(m_glfwWindow);
    }

    public void destroy() {
        if(m_glfwWindow != NULL)
            glfwDestroyWindow(m_glfwWindow);
    }

    public void updateDisplayTitle(String updatedTitle) {
        glfwSetWindowTitle(m_glfwWindow, updatedTitle);
    }

    public void closeWindow() {
        glfwSetWindowShouldClose(m_glfwWindow, true);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(m_glfwWindow);
    }


    public long glfwWindow() {
        return m_glfwWindow;
    }

    public void setSettings(Settings settings) {
        m_Settings.title = settings.title;

        m_Settings.width = settings.width;
        m_Settings.height = settings.height;
        m_Settings.vBlanks = settings.vBlanks;

        m_Settings.useVSync = settings.useVSync;
        m_Settings.resizable = settings.resizable;
        m_Settings.fullscreen = settings.fullscreen;
    }

    public Settings getSettings() {
        return m_Settings;
    }

    /**
     * A simple static class that lets us set up a window with a set of defined variables.
     *
     * @author Tahnner Shambaugh (ArcaneSunku)
     * @date 4/16/2022
     */
    public static class Settings {

        public String title = "Window";

        public int width = 860;
        public int height = 480;
        public int vBlanks = 1;

        public boolean useVSync = true;
        public boolean resizable = false;
        public boolean fullscreen = false;

    }

}
