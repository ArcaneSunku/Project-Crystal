package git.crystal.engine;

/**
 * A simple interface that lets us inject our custom logic into our Game Engine.
 * This gives us the necessary control we need to create a game.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/16/2022
 */

public interface IGame {

    /**
     * This method is called before our game loop starts up. Implementing this is important for making sure
     * your project has everything created before it is used. Especially things that are used over and over.
     */
    void initialize();

    /**
     * This method is where you will want to call any logical or input logic. Implementing this is important
     * for more than just input, but also changing variables that could be used by our renderer.
     */
    void update();

    /**
     * This method handles all of our rendering logic directly. Implementing this is important for every visual aspect
     * of your project. This is called after update so any variable changes from there will carry over.
     * @param alpha this is essentially, the time it takes to render or rather, how much progress we've made on when we should render.
     */
    void render(float alpha);

    /**
     * This method is meant to handle disposing everything for your personal project. Implementing this will have everything in your
     * project disposed before our systems and everything shut down.
     */
    void dispose();

}
