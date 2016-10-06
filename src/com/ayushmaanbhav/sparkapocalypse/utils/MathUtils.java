package com.ayushmaanbhav.sparkapocalypse.utils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Math utils
 * @author ayush
 */
public class MathUtils {

    /**
     * Rotate a vector about an axis
     * @param vectorToRotate
     * @param axis
     * @param cosTheta
     * @param sinTheta
     * @return
     */
    public static Vector3D rotateVector(Vector3D vectorToRotate, Vector3D axis, double cosTheta, double sinTheta) {
        double x = axis.getX();
        double y = axis.getY();
        double z = axis.getZ();
        double xSquare = x * x;
        double ySquare = y * y;
        double zSquare = z * z;
        double xy = x * y;
        double yz = y * z;
        double zx = z * x;
        double oneMinusCosTheta = 1 - cosTheta;

        RealMatrix rotationMatrix = MatrixUtils.createRealMatrix(3, 3);
        rotationMatrix.setEntry(0, 0, cosTheta + xSquare * oneMinusCosTheta);
        rotationMatrix.setEntry(0, 1, xy * oneMinusCosTheta - z * sinTheta);
        rotationMatrix.setEntry(0, 2, zx * oneMinusCosTheta + y * sinTheta);
        rotationMatrix.setEntry(1, 0, xy * oneMinusCosTheta + z * sinTheta);
        rotationMatrix.setEntry(1, 1, cosTheta + ySquare * oneMinusCosTheta);
        rotationMatrix.setEntry(1, 2, yz * oneMinusCosTheta - x * sinTheta);
        rotationMatrix.setEntry(2, 0, zx * oneMinusCosTheta - y * sinTheta);
        rotationMatrix.setEntry(2, 1, yz * oneMinusCosTheta + x * sinTheta);
        rotationMatrix.setEntry(2, 2, cosTheta + zSquare * oneMinusCosTheta);

        return new Vector3D(rotationMatrix.operate(vectorToRotate.toArray())).normalize();
    }

    /**
     * Fast dot product
     * @param v1 Vector3D
     * @param v2 Vector3D
     * @return dotProduct
     */
    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

}
