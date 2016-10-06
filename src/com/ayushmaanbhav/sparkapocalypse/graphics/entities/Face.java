package com.ayushmaanbhav.sparkapocalypse.graphics.entities;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import lombok.Getter;

/**
 * Face of an object model.
 * Not threadsafe
 * 
 * @author ayush
 */
@Getter
public class Face {

    private final Vector3D vertices[];
    private final Vector2D vertexTextures[];
    private Vector3D vertexNormals[];
    private final int noOfVertices;
    private int count;

    private final Vector2D projectedVertices[];
    private ProjectionPlane projectionPlane;
    private Vector3D normalVector;

    public Face(int noOfVertices) {
        if (noOfVertices < 3) {
            throw new RuntimeException("Face should have atleast 3 vertices");
        }
        this.noOfVertices = noOfVertices;
        this.count = 0;
        this.vertices = new Vector3D[noOfVertices];
        this.vertexTextures = new Vector2D[noOfVertices];
        this.vertexNormals = null;
        this.projectedVertices = new Vector2D[noOfVertices];
        this.projectionPlane = null;
        this.normalVector = null;
    }

    public void addVertex(Vector3D vertex, Vector2D vertexTexture, Vector3D vertexNormal) {
        if (count >= noOfVertices) {
            throw new IllegalArgumentException("Size limit breached");
        }

        this.vertices[count] = vertex;
        this.vertexTextures[count] = vertexTexture;

        if (vertexNormal != null) {
            if (this.vertexNormals == null) {
                this.vertexNormals = new Vector3D[noOfVertices];
            }
            this.vertexNormals[count] = vertexNormal;
        }

        count++;

        if (count == noOfVertices) {
            computeMinRange();
            computeNormalVector();
        }
    }

    private void computeNormalVector() {
        try {
            normalVector = vertices[1].subtract(vertices[0])
                    .crossProduct(vertices[2].subtract(vertices[0]))
                    .normalize();
        } catch (MathArithmeticException e) {
            // some faces are giving problems probably due to some errors in obj files
        }
    }

    private void computeMinRange() {

        double rangeX = Arrays.stream(vertices)
                .mapToDouble(Vector3D::getX)
                .max()
                .getAsDouble()
                - Arrays.stream(getVertices())
                .mapToDouble(Vector3D::getX)
                .min()
                .getAsDouble();

        double rangeY = Arrays.stream(vertices)
                .mapToDouble(Vector3D::getY)
                .max()
                .getAsDouble()
                - Arrays.stream(getVertices())
                .mapToDouble(Vector3D::getY)
                .min()
                .getAsDouble();

        double rangeZ = Arrays.stream(vertices)
                .mapToDouble(Vector3D::getZ)
                .max()
                .getAsDouble()
                - Arrays.stream(getVertices())
                .mapToDouble(Vector3D::getZ)
                .min()
                .getAsDouble();

        double minRange = Math.min(rangeZ, Math.min(rangeX, rangeY));

        if (minRange == rangeX) {
            projectionPlane = ProjectionPlane.YZ;
            for (int i = 0; i < noOfVertices; i++) {
                projectedVertices[i] = new Vector2D(vertices[i].getY(), vertices[i].getZ());
            }
        } else if (minRange == rangeY) {
            projectionPlane = ProjectionPlane.XZ;
            for (int i = 0; i < noOfVertices; i++) {
                projectedVertices[i] = new Vector2D(vertices[i].getX(), vertices[i].getZ());
            }
        } else {
            projectionPlane = ProjectionPlane.XY;
            for (int i = 0; i < noOfVertices; i++) {
                projectedVertices[i] = new Vector2D(vertices[i].getX(), vertices[i].getY());
            }
        }
    }

    public Optional<Vector3D[]> getVertexNormals() {
        return Optional.ofNullable(this.vertexNormals);
    }

    public Optional<Vector3D> getNormalVector() {
        return Optional.ofNullable(this.normalVector);
    }

    public static enum ProjectionPlane {
        XY, YZ, XZ;
    }

}