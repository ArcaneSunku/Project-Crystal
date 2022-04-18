package git.crystal.engine;

import git.crystal.engine.gfx.Window;
import git.crystal.engine.utils.Timer;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

/**
 * The main bread and butter, this class holds all of our logic for our "Engine" to function with any external project.
 * This will be where everything important is created, called, iterated, and disposed of. So far we handle how our thread
 * handles updating frames with Java's API if for some reason GLFW's swapInterval is set to 0.
 *
 * It's important to know that this Engine operates with a Fixed Time Loop
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/16/2022
 */

public class CrystalEngine implements Runnable {

    private static final int TARGET_FPS = 75;
    private static final int TARGET_UPS = 60;

    private final Timer m_Timer;
    private final Window m_Window;

    private IGame m_game;
    private Thread m_gameThread;
    private GLFWErrorCallback m_errorCallback;

    private volatile boolean mv_running;

    /**
     * Initializes our class with a basic set up for our Settings subclass.
     */
    public CrystalEngine() {
        this(new Window.Settings());
    }

    /**
     * In our constructor, we instantiate our Window Singleton and our Timer class.
     * This is, without a doubt required to have everything running.
     *
     * @param settings the custom settings our client might want to set up first
     */
    public CrystalEngine(Window.Settings settings) {
        m_Window = Window.Instance();
        m_Window.setSettings(settings);

        m_Timer = new Timer();
        mv_running = false;
    }

    /**
     * This method does as it is named, starts the required processes for our game to run and function.
     * To that extent we also pass in our client's game.
     * @param game we'll take in the class that implements our IGame to inject custom functionality
     */
    public synchronized void start(IGame game) {
        if(m_gameThread != null || mv_running) {
            System.err.println("An instance of the engine/game is already running!");
            return;
        }

        m_gameThread = new Thread(this, "Main_Thread");
        m_game = game;
        m_gameThread.start();
    }

    private synchronized void stop() {
        try {
            m_Window.destroy();
            glfwTerminate();
            m_errorCallback.free();

            m_gameThread.join(1);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initialize() {
        m_errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(m_errorCallback);

        if(!m_Window.create()) {
            System.err.println("FATAL ERROR: Failed to create a window to display!");
            stop();
        }

        m_Timer.start();
        m_game.initialize();
        m_Window.display();

        mv_running = true;
    }

    private void gameLoop() {
        // Set up variables for our Fixed Time Loop
        float delta, alpha;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        while(mv_running) {
            if(m_Window.shouldClose()) {
                mv_running = false;
            }

            delta = m_Timer.getDelta();
            accumulator += delta;

            while(accumulator >= interval) {
                update();
                m_Timer.updateUPS();
                accumulator -= interval;
            }

            alpha = accumulator / interval;

            render(alpha);

            m_Timer.updateFPS();
            m_Timer.update();

            if(m_Timer.getTimerCount() >= 100) {
                System.out.printf("FPS: %d, UPS: %d%n", m_Timer.getFPS(), m_Timer.getUPS());
                m_Timer.resetTimer();
            }

            m_Window.update();

            if(!m_Window.getSettings().useVSync)
                sync();

        }
    }

    private void update() {
        m_game.update();
        m_Timer.updateTimerCount();
    }

    private void render(float alpha) {
        m_game.render(alpha);
    }

    private void dispose() {
        m_game.dispose();
        stop();
    }

    @Override
    public void run() {
        initialize();
        gameLoop();
        dispose();
    }

    private void sync() {
        double lastLoopTime = m_Timer.lastLoopTime();
        double now = m_Timer.getTime();
        float targetTime = 1f / CrystalEngine.TARGET_FPS;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Logger.getLogger(CrystalEngine.class.getName()).log(Level.SEVERE, null, e);
            }

            now = m_Timer.getTime();
        }
    }
}
