package git.crystal.engine.utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * A class created to maintain the Game Loop and give us the capability of keeping track of FPS.
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/16/2022
 */
public class Timer {

    private double _lastLoopTime;
    private float _timeCount;
    private int _timerCount;

    private int _fps, _fpsCount;
    private int _ups, _upsCount;

    public void start() {
        _lastLoopTime = getTime();
    }

    public void update() {
        if(_timeCount > 1f) {
            _fps = _fpsCount;
            _fpsCount = 0;

            _ups = _upsCount;
            _upsCount = 0;

            _timeCount -= 1f;
        }
    }

    public void updateFPS() {
        _fpsCount++;
    }

    public void updateUPS() {
        _upsCount++;
    }

    public void updateTimerCount() { _timerCount++; }

    public void resetTimer() { _timerCount = 0; }

    public float getDelta() {
        double time = getTime();
        float delta = (float) (time - _lastLoopTime);
        _lastLoopTime = time;
        _timeCount += delta;

        return delta;
    }

    public double getTime() {
        return glfwGetTime();
    }

    public int getFPS() {
        return _fps > 0 ? _fps : _fpsCount;
    }

    public int getUPS() {
        return _ups > 0 ? _ups : _upsCount;
    }

    public int getTimerCount() { return _timerCount; }

    public double lastLoopTime() {
        return _lastLoopTime;
    }

}
