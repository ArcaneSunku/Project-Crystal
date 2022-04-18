package git.crystal.example;

import git.crystal.engine.CrystalEngine;
import git.crystal.engine.IGame;
import git.crystal.engine.gfx.Shader;
import git.crystal.engine.gfx.Window;

public class Example implements IGame {

    public final Shader shader;
    public final Window.Settings settings;

    private Example() {
        shader = new Shader("/assets/shaders/basic.vert", "/assets/shaders/basic.frag");
        settings = new Window.Settings();

        settings.title = "Arcane Crystal | v0.01 InDev";
        settings.useVSync = false;
    }

    @Override
    public void initialize() {
        shader.create();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(float alpha) {

    }

    @Override
    public void dispose() {

    }

    public static void main(String[] args) {
        final Example game = new Example();
        new CrystalEngine(game.settings).start(game);
    }
}
