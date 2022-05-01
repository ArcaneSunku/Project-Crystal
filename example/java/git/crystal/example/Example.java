package git.crystal.example;

import git.crystal.engine.CrystalEngine;
import git.crystal.engine.GameObject;
import git.crystal.engine.IGame;
import git.crystal.engine.input.KeyboardInput;
import git.crystal.engine.input.MouseInput;
import git.crystal.engine.render.Mesh;
import git.crystal.engine.render.Renderer;
import git.crystal.engine.render.Shader;
import git.crystal.engine.render.Texture;
import git.crystal.engine.render.ui.Window;
import git.crystal.engine.render.util.Camera;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Example implements IGame {

    private static final float MOUSE_SENSITIVITY = 5f;
    private static final float CAMERA_SPEED = 2f;

    private final Camera m_Camera;
    private final List<GameObject> m_Objects;
    private final Renderer m_Renderer;
    public final Window.Settings m_Settings;

    private Example() {
        final Shader shader = new Shader("/assets/shaders/basic.vert", "/assets/shaders/basic.frag");

        m_Camera = new Camera();
        m_Objects = new ArrayList<>();
        m_Renderer = new Renderer(shader);
        m_Settings = new Window.Settings();

        m_Settings.title = "Arcane Crystal | v0.01 InDev";
        m_Settings.useVSync = true;
    }

    @Override
    public void initialize() {
        m_Renderer.initialize();
        m_Camera.movePosition(0, 0, 1);

        final float[] vertices = new float[] {
            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,
             1.0f, -1.0f,  1.0f,
             1.0f,  1.0f,  1.0f,
        };

        final float[] textureCoords = new float[] {
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f,
        };

        final int[] indices = new int[] {
            0, 1, 3,
            3, 1, 2,
        };

        final Texture testTexture = Texture.loadTexture("assets/textures/testing.jpg");
        final Mesh texturedMesh = new Mesh(vertices, textureCoords, indices, testTexture);

        final GameObject testObj1 = new GameObject(texturedMesh);
        testObj1.setPosition(0, 0, -2);

        final GameObject testObj2 = new GameObject(texturedMesh);
        testObj2.setPosition(-2, 0, -6);

        m_Objects.add(testObj1);
        m_Objects.add(testObj2);
    }

    private final Vector3f cameraInc = new Vector3f(0, 0, 0);
    @Override
    public void update(float deltaTime) {
        cameraInc.set(0, 0, 0);

        if(KeyboardInput.isKeyPressed(GLFW_KEY_W))
            cameraInc.z = -1;
        else if(KeyboardInput.isKeyPressed(GLFW_KEY_S))
            cameraInc.z = 1;

        if(KeyboardInput.isKeyPressed(GLFW_KEY_A))
            cameraInc.x = -1;
        else if(KeyboardInput.isKeyPressed(GLFW_KEY_D))
            cameraInc.x = 1;

        if(KeyboardInput.isKeyPressed(GLFW_KEY_Q))
            cameraInc.y = -1;
        else if(KeyboardInput.isKeyPressed(GLFW_KEY_E))
            cameraInc.y = 1;

        float adjustedCamSpeed = CAMERA_SPEED * deltaTime;
        m_Camera.movePosition(cameraInc.x * adjustedCamSpeed, cameraInc.y * adjustedCamSpeed, cameraInc.z * adjustedCamSpeed);

        if(MouseInput.isRightPressed()) {
            Vector2f rotVec = MouseInput.getDisplayVec();
            float adjustedSpeed = MOUSE_SENSITIVITY * deltaTime;
            m_Camera.moveRotation(rotVec.x * adjustedSpeed, rotVec.y * adjustedSpeed, 0);
        }
    }

    @Override
    public void render(float alpha) {
        m_Renderer.draw(m_Objects, m_Camera);
    }

    @Override
    public void dispose() {
        for(GameObject object : m_Objects)
            object.getMesh().cleanup();

        m_Renderer.cleanup();
    }

    public static void main(String[] args) {
        final Example game = new Example();
        new CrystalEngine(game.m_Settings).start(game);
    }
}
