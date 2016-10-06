package com.ayushmaanbhav.sparkapocalypse.physics.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.ayushmaanbhav.sparkapocalypse.config.AppConfig;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Face;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Face.ProjectionPlane;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.World;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.PhysicalObject;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.SparkParticle;
import com.ayushmaanbhav.sparkapocalypse.utils.MathUtils;

/**
 * Collision detector engine
 * @author ayush
 */
public class CollisionDectector {

    private final CollisionHandler collisionHandler;

    public CollisionDectector(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }

    /**
     * Apply collision detection on this world
     * Detect and resolve collisions between sparks and models
     * @param world - World
     */
    public void run(World world) {

        ListIterator<SparkParticle> sparkParticleIterator = world.getSparkParticles().listIterator();
        List<SparkParticle> newParticles = new ArrayList<>(5000);

        while (sparkParticleIterator.hasNext()) {
            SparkParticle sparkParticle = sparkParticleIterator.next();

            for (PhysicalObject physicalObject : world.getPhysicalObjects()) {
                double x = sparkParticle.getPosition().getX();
                double y = sparkParticle.getPosition().getY();
                double z = sparkParticle.getPosition().getZ();

                Vector3D[] bounds = physicalObject.getAxisAlignedBoundingBox();
                if (x >= bounds[0].getX() && x <= bounds[1].getX()
                        && y >= bounds[0].getY() && y <= bounds[1].getY()
                        && z >= bounds[0].getZ() && z <= bounds[1].getZ()) {

                    /* Using distance algorithm instead of ray casting as:
                     * 1: Collisions are being detected after they cross the surface of the object so the blast and splitting is happening inside the object
                     * 2: Distance based algorithm is more efficient, most probably because there are a few faces for now
                     * 
                     * UPDATE!!!!
                     * The distance algorithm is not working so well though its taking a bit less time
                     * 1: Particles are bouncing of way before hitting the surface no matter how much i adjust the threshold
                     * something or the other is being affected (one solution is to keep individual thresholds for each model)
                     * 2: I retried ray casting algorithm with updates time manager with predictions so that i am able to get the
                     * next possible position of the particle which i feed to the algorithm instead of the current. its working accuratey now!!
                     * 
                     */

                    Face face = checkForAllFacesWithRayCastingAlgorithm(sparkParticle.getPossibleNextPosition(), physicalObject.getModel().getFaces(),
                            AppConfig.COLLISION_RAY_CASTING_REFERENCE_VECTOR);

                    // collision occurred
                    if (face != null) {
                        newParticles.addAll(collisionHandler.handleCollision(physicalObject, face, sparkParticle));

                        sparkParticleIterator.remove();
                        break;
                    }
                }
            }
        }

        world.addSparkParticles(newParticles);
    }

    /**
     * Check if a point lies in a polyhedron
     * 
     * Draw a virtual line from referencePoint to the position and check how many times intersected
     * If intersected times is odd then its inside the object
     * 
     * @param position - point
     * @param faces - faces of polyhedron
     * @param referencePoint - referencePoint
     */
    private Face checkForAllFacesWithRayCastingAlgorithm(Vector3D position, List<Face> faces, Vector3D referencePoint) {

        int interactionCount = 0;

        Vector3D lineVector = position.subtract(referencePoint).normalize();

        for (Face face : faces) {

            if (! face.getNormalVector().isPresent()) {
                // some normal vectors are giving problems hence ignoring these faces
                continue;
            }

            Vector3D normalVector = face.getNormalVector().get();

            // point and COLLISION_LINE_VECTOR should be on opposite sides of the plane
            double result1 = MathUtils.dotProduct(position.subtract(face.getVertices()[0]), normalVector);
            double result2 = MathUtils.dotProduct(referencePoint.subtract(face.getVertices()[0]), normalVector);

            // assuming result2 will never be 0
            if (!(result1 == 0 || (result1 > 0 && result2 < 0) || (result1 < 0 && result2 > 0))) {
                continue;
            }

            // get the point of intersection
            // lambda = (p0 - l0).n/l.n
            // p0 - point on plane, l0 - point on line, n - normal vector of plane, l - vector parallel to line
            double lambda = MathUtils.dotProduct(face.getVertices()[0].subtract(referencePoint), normalVector)
                    / MathUtils.dotProduct(normalVector, lineVector);

            Vector3D pointOfIntersection = lineVector.scalarMultiply(lambda).add(referencePoint);

            // check if this point lies between all the vertices of the polygon formed by the faces
            // going in order the point should line on the same side of all edges
            // the face is a convex polygon so applying this algo

            // project the given face onto 2D deleting smallest range coordinate

            Vector2D[] projectedVertices = face.getProjectedVertices();
            Vector2D projectedPoint = null;
            if (face.getProjectionPlane() == ProjectionPlane.YZ) {
                projectedPoint = new Vector2D(pointOfIntersection.getY(), pointOfIntersection.getZ());
            }
            else if (face.getProjectionPlane() == ProjectionPlane.XZ) {
                projectedPoint = new Vector2D(pointOfIntersection.getX(), pointOfIntersection.getZ());
            }
            else {
                projectedPoint = new Vector2D(pointOfIntersection.getX(), pointOfIntersection.getY());
            }

            // check if the point lie on the same side of all the edges
            int positives = 0;
            int negatives = 0;
            for (int i = 0; i < face.getNoOfVertices(); i++) {
                Vector2D point1 = projectedVertices[i];
                Vector2D point2 = projectedVertices[(i + 1) % face.getNoOfVertices()];

                double result = (point2.getX() - point1.getX()) * (projectedPoint.getY() - point1.getY())
                        - (point2.getY() - point1.getY()) * (projectedPoint.getX() - point1.getX());

                if (result == 0) {
                    // collision detected
                    return face;
                } else if (result > 0) {
                    ++positives;
                } else {
                    ++negatives;
                }
            }

            if (positives > 0 && negatives > 0) {
                continue;
            }

            ++interactionCount;
        }

        if (interactionCount % 2 == 0) {
            return null;
        }

        // point inside the polyhedron so get the closest face
        return getClosestFace(position, faces);
    }

    /**
     * Check if a point lies within the threshold of a given face. If yes then its a collision
     * 
     * Easy and takes less time. Why didn't I think of it first!!
     * Probably due to the fact that I wanted to apply all the graphics algos in this project hehe.. ^_^
     * 
     * @param position - point
     * @param faces - faces of polyhedron
     */
    @SuppressWarnings("unused")
    private Face checkForAllFacesWithDistanceFromPlaneBasedAlgorithm(Vector3D position, List<Face> faces) {

        for (Face face : faces) {

            if (! face.getNormalVector().isPresent()) {
                // some normal vectors are giving problems hence ignoring these faces
                continue;
            }

            Vector3D normalVector = face.getNormalVector().get();

            double distance = Math.abs(MathUtils.dotProduct(position.subtract(face.getVertices()[0]), normalVector));

            if (distance < AppConfig.COLLISION_DISTANCE_THRESHOLD) {
                return face;
            }
        }

        return null;
    }

    private Face getClosestFace(Vector3D position, List<Face> faces) {

        double minDistance = Double.MAX_VALUE;
        Face faceWithMinDistance = null;

        for (Face face : faces) {
            double distance = 0;
            for (Vector3D vertex : face.getVertices()) {
                distance += Vector3D.distanceSq(position, vertex);
            }
            if (distance < minDistance) {
                minDistance = distance;
                faceWithMinDistance = face;
            }
        }

        return faceWithMinDistance;
    }

}
