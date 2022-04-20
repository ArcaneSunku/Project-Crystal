package git.crystal.engine.render.util;

import org.joml.Vector3f;

/**
 * Represents a "physical" Camera in our simulation. This way we can "move" around and explore our
 * creation!
 *
 * @author Tahnner Shambaugh (ArcaneSunku)
 * @date 4/19/2022
 */

public class Camera {

    private final Vector3f m_Position, m_Rotation;

    public Camera() {
        this(new Vector3f(), new Vector3f());
    }

    public Camera(Vector3f position, Vector3f rotation) {
        m_Position = position;
        m_Rotation = rotation;
    }

    public void movePosition(float xOffs, float yOffs, float zOffs) {
        if ( zOffs != 0 ) {
            m_Position.x += (float)Math.sin(Math.toRadians(m_Rotation.y)) * -1.0f * zOffs;
            m_Position.z += (float)Math.cos(Math.toRadians(m_Rotation.y)) * zOffs;
        }

        if ( xOffs != 0) {
            m_Position.x += (float)Math.sin(Math.toRadians(m_Rotation.y - 90)) * -1.0f * xOffs;
            m_Position.z += (float)Math.cos(Math.toRadians(m_Rotation.y - 90)) * xOffs;
        }

        m_Position.y += yOffs;
    }

    public void moveRotation(float xOffs, float yOffs, float zOffs) {
        m_Rotation.x += xOffs;
        m_Rotation.y += yOffs;
        m_Rotation.z += zOffs;
    }

    public void setPosition(float xPos, float yPos, float zPos) {
        m_Position.x = xPos;
        m_Position.y = yPos;
        m_Position.z = zPos;
    }

    public void setRotation(float xRot, float yRot, float zRot) {
        m_Rotation.x = xRot;
        m_Rotation.y = yRot;
        m_Rotation.z = zRot;
    }

    public Vector3f getPosition() {
        return m_Position;
    }

    public Vector3f getRotation() {
        return m_Rotation;
    }

}
