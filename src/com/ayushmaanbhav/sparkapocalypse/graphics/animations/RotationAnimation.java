package com.ayushmaanbhav.sparkapocalypse.graphics.animations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Model;
import com.jogamp.opengl.GL2;

/**
 * Rotation animation on an object about its center about the z axis
 * @author ayush
 */
public class RotationAnimation implements Animation {

    private final Vector3D center;
    private final long noOfVertices;

    public RotationAnimation(Model model) {
        List<Vector3D> vertices = model.getFaces()
                .stream()
                .flatMap(face -> Arrays.stream((face.getVertices())))
                .distinct()
                .collect(Collectors.toList());

        this.noOfVertices = vertices.size();

        this.center = vertices.stream()
                .reduce(Vector3D::add)
                .get()
                .scalarMultiply(1 / (double) noOfVertices);
    }

    @Override
    public void apply(GL2 gl) {
        gl.glTranslated(center.getX(), center.getY(), center.getZ());
        gl.glRotated(-(System.currentTimeMillis() % 360), 0, 0, 1);
        gl.glTranslated(-center.getX(), -center.getY(), -center.getZ());
    }

}
