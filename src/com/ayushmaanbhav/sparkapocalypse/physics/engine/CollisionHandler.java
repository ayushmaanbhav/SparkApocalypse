package com.ayushmaanbhav.sparkapocalypse.physics.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.ayushmaanbhav.sparkapocalypse.config.AppConfig;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Face;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.PhysicalObject;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.SparkParticle;

/**
 * Contains logic for result of collision of spark particle like change in momentum and splitting
 * @author ayush
 */
public class CollisionHandler {

    private final SparkGenerator sparkGenerator;

    public CollisionHandler(SparkGenerator sparkGenerator) {
        this.sparkGenerator = sparkGenerator;
    }

    /**
     * Handle collision
     * @param physicalObject - PhysicalObject
     * @param face - Face on which collision occurred
     * @param sparkParticle - collided particle
     */
    public List<SparkParticle> handleCollision(PhysicalObject physicalObject, Face face, SparkParticle sparkParticle) {

        Vector3D normalVector = face.getNormalVector().get();
        Vector3D vectorToReflect = sparkParticle.getVelocity();

        // correct normal vector's orientation
        double distance1 = normalVector.distance(vectorToReflect.normalize());
        double distance2 = normalVector.negate().distance(vectorToReflect.normalize());

        if (distance2 > distance1) {
            normalVector = normalVector.negate();
        }

        // get reflected vector
        Vector3D reflectedVector = vectorToReflect.subtract(normalVector
                .scalarMultiply(2 * vectorToReflect.dotProduct(normalVector))).normalize();

        // now apply the rule of inelastic collision to massive objects
        Vector3D newVelocity = reflectedVector.scalarMultiply(sparkParticle.getVelocity().getNorm()
                * physicalObject.getRestitutionCoefficient()
                * sparkParticle.getRestitutionCoefficient());

        sparkParticle.setVelocity(newVelocity);
        sparkParticle.setCollided(true);

        List<SparkParticle> newParticles = new ArrayList<>();
        newParticles.add(sparkParticle);

        // Spark particle can split too
        double splitProbablity = Math.random();
        if (splitProbablity < AppConfig.SPARK_PARTICLE_SPLIT_PROBABLITY) {
            // Splitting upto SPARK_PARTICLE_SPLIT_MAX particles
            int noOfResultingParticles = (int) ((AppConfig.SPARK_PARTICLE_SPLIT_MAX - 1) * Math.random() + 2);

            newParticles.addAll(sparkGenerator.generateSparkParticlesFromParent(sparkParticle, noOfResultingParticles));
            newParticles.remove(sparkParticle);
        }

        return newParticles;
    }

}
