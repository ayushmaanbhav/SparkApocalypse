package com.ayushmaanbhav.sparkapocalypse.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.ayushmaanbhav.sparkapocalypse.config.AppConfig;
import com.ayushmaanbhav.sparkapocalypse.utils.MathUtils;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

/**
 * Controller for the camera based on user input
 * @author ayush
 */
public class CameraController implements KeyListener, MouseMotionListener {

    private Vector3D cameraPosition;
    private Vector3D cameraOrientation;
    private Vector3D cameraLookAt;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveForward;
    private boolean moveBack;

    private Vector2D oldMousePosition;
    private Vector2D newMousePosition;

    private final double moveFactor;
    private final double rotateFactor;
    
    public CameraController() {
        cameraPosition = AppConfig.CAMERA_POSITION;
        cameraOrientation = AppConfig.CAMERA_ORIENTATION;
        cameraLookAt = AppConfig.CAMERA_LOOK_AT;

        if (cameraLookAt.equals(cameraPosition)) {
            throw new RuntimeException("Camera look at cant be same as camera position");
        }

        moveFactor = AppConfig.CAMERA_MOVE_FACTOR;
        rotateFactor = AppConfig.CAMERA_ROTATE_FACTOR;

        moveLeft = moveRight = moveForward = moveBack = false;

        oldMousePosition = newMousePosition = null;
    }

    public void setupCamera(GL2 gl, GLU glu) {
        // do movements
        // get camera directions
        Vector3D cameraDirection = cameraLookAt.subtract(cameraPosition).normalize();
        Vector3D cameraLeftDirection = cameraOrientation.crossProduct(cameraDirection).normalize();

        if (moveForward) {
            cameraPosition = cameraPosition.add(cameraDirection.scalarMultiply(moveFactor));
            cameraLookAt = cameraLookAt.add(cameraDirection.scalarMultiply(moveFactor));
        }
        if (moveBack) {
            cameraPosition = cameraPosition.subtract(cameraDirection.scalarMultiply(moveFactor));
            cameraLookAt = cameraLookAt.subtract(cameraDirection.scalarMultiply(moveFactor));
        }

        if (moveLeft) {
            cameraPosition = cameraPosition.add(cameraLeftDirection.scalarMultiply(moveFactor));
            cameraLookAt = cameraLookAt.add(cameraLeftDirection.scalarMultiply(moveFactor));
        }
        if (moveRight) {
            cameraPosition = cameraPosition.subtract(cameraLeftDirection.scalarMultiply(moveFactor));
            cameraLookAt = cameraLookAt.subtract(cameraLeftDirection.scalarMultiply(moveFactor));
        }

        // do rotations
        if (oldMousePosition != null && newMousePosition != null && !oldMousePosition.equals(newMousePosition)) {
            Vector2D directionOfMovement = newMousePosition.subtract(oldMousePosition).normalize();
            directionOfMovement = new Vector2D(directionOfMovement.getX(), -1 * directionOfMovement.getY());

            Vector3D directionRelativeToCamera = MathUtils.rotateVector(cameraLeftDirection.negate(),
                    cameraDirection, directionOfMovement.getX(), directionOfMovement.getY());

            cameraLookAt = cameraLookAt.add(directionRelativeToCamera.scalarMultiply(rotateFactor));
        }

        oldMousePosition = newMousePosition;

        glu.gluLookAt(
                cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(),
                cameraLookAt.getX(), cameraLookAt.getY(), cameraLookAt.getZ(),
                cameraOrientation.getX(), cameraOrientation.getY(), cameraOrientation.getZ());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            moveForward = true;
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            moveBack = true;
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            moveLeft = true;
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            moveRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            moveForward = false;
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            moveBack = false;
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            moveLeft = false;
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            moveRight = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        newMousePosition = new Vector2D(e.getX(), -e.getY());
    }

    // unwanted methods    
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
