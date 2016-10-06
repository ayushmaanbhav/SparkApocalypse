package com.ayushmaanbhav.sparkapocalypse.physics.entities;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Model;

import lombok.Value;

/**
 * Model with physical properties
 * @author ayush
 */
@Value
public class PhysicalObject {

    private final Model model;

    // Physical properties
    private final float restitutionCoefficient;

    private Vector3D[] axisAlignedBoundingBox;
    private final double boundingBoxOffset;

    public PhysicalObject(Model model, float restitutionCoefficient, double boundingBoxOffset) {
        super();
        this.model = model;
        this.restitutionCoefficient = restitutionCoefficient;
        this.boundingBoxOffset = boundingBoxOffset;

        this.axisAlignedBoundingBox = new Vector3D[2];
        calcluateMaxAndMinPoints();
    }

    private void calcluateMaxAndMinPoints() {

        List<Vector3D> vertices = model.getFaces()
                .stream()
                .flatMap(face -> Arrays.stream((face.getVertices())))
                .distinct()
                .collect(Collectors.toList());

        double maxX = vertices.stream()
                .mapToDouble(Vector3D::getX)
                .max()
                .getAsDouble();
        double maxY = vertices.stream()
                .mapToDouble(Vector3D::getY)
                .max()
                .getAsDouble();
        double maxZ = vertices.stream()
                .mapToDouble(Vector3D::getZ)
                .max()
                .getAsDouble();

        double minX = vertices.stream()
                .mapToDouble(Vector3D::getX)
                .min()
                .getAsDouble();
        double minY = vertices.stream()
                .mapToDouble(Vector3D::getY)
                .min()
                .getAsDouble();
        double minZ = vertices.stream()
                .mapToDouble(Vector3D::getZ)
                .min()
                .getAsDouble();

        double offset = boundingBoxOffset;
        axisAlignedBoundingBox[0] = new Vector3D(minX - offset, minY - offset, minZ - offset);
        axisAlignedBoundingBox[1] = new Vector3D(maxX + offset, maxY + offset, maxZ + offset);

    }

}
