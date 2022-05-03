package git.crystal.engine;

import git.crystal.engine.render.mesh.Mesh;
import org.joml.Vector3f;

/**
 * Replace with description for GameObject
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/19/2022
 */

public class GameObject {

    private final Mesh m_Mesh;
    private final Vector3f m_Position, m_Scale, m_Rotation;

    public GameObject(Mesh mesh) {
        m_Mesh = mesh;

        m_Position = new Vector3f();
        m_Rotation = new Vector3f();

        m_Scale    = new Vector3f(1, 1, 1);
    }

    public void setPosition(float x, float y, float z) {
        m_Position.x = x;
        m_Position.y = y;
        m_Position.z = z;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        m_Scale.x = scaleX;
        m_Scale.y = scaleY;
        m_Scale.z = scaleZ;
    }

    public void setRotation(float rotateX, float rotateY, float rotateZ) {
        m_Rotation.x = rotateX;
        m_Rotation.y = rotateY;
        m_Rotation.z = rotateZ;
    }

    public void setPosition(Vector3f position) {
        setPosition(position.x(), position.y(), position.z());
    }

    public void setScale(Vector3f scale) {
        setScale(scale.x(), scale.y(), scale.z());
    }

    public void setRotation(Vector3f rotation) {
        setRotation(rotation.x(), rotation.y(), rotation.z());
    }

    public Vector3f getPosition() {
        return m_Position;
    }

    public Vector3f getScale() {
        return m_Scale;
    }

    public Vector3f getRotation() {
        return m_Rotation;
    }

    public Mesh getMesh() {
        return m_Mesh;
    }

}
