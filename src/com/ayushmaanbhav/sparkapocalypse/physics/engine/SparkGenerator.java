package com.ayushmaanbhav.sparkapocalypse.physics.engine;

import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.FORWARD_VELOCITY_MAGNITUDE_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.NO_OF_SPARKS_TO_GENERATE_EACH_ITERATION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPARK_BIRTH_POINT_DIFFUSE_FACTOR_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPARK_LIFE_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPARK_MASS_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPARK_PARTICLE_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPARK_PARTICLE_SPLIT_SIDEWAYS_VELOCITY;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPARK_START_POINT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPARK_TAIL_LENGTH_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPLIT_SPARK_LIFE_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.X_FORWARD_NORMAL_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.Y_FORWARD_NORMAL_DISTRIBUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.Z_FORWARD_NORMAL_DISTRIBUTION;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.ayushmaanbhav.sparkapocalypse.physics.entities.SparkParticle;
import com.ayushmaanbhav.sparkapocalypse.utils.MathUtils;

/**
 * Contains the logic for initializing spark particles
 * @author ayush
 */
public class SparkGenerator {

    private final RealDistribution xForwardNormalDistribution;
    private final RealDistribution yForwardNormalDistribution;
    private final RealDistribution zForwardNormalDistribution;
    private final RealDistribution forwardVelocityMagnitudeDistribution;
    private final RealDistribution sparkLifeDistribution;
    private final RealDistribution splitSparkLifeDistribution;
    private final RealDistribution sparkMassDistribution;
    private final RealDistribution sparkTailLengthDistribution;
    private final RealDistribution sparkBirthPointDiffuseFactorDistribution;

    private final TimeManager timeManager;

    public SparkGenerator(TimeManager timeManager) {
        this.timeManager = timeManager;

        this.xForwardNormalDistribution = X_FORWARD_NORMAL_DISTRIBUTION;
        this.yForwardNormalDistribution = Y_FORWARD_NORMAL_DISTRIBUTION;
        this.zForwardNormalDistribution = Z_FORWARD_NORMAL_DISTRIBUTION;
        this.forwardVelocityMagnitudeDistribution = FORWARD_VELOCITY_MAGNITUDE_DISTRIBUTION;
        this.sparkLifeDistribution = SPARK_LIFE_DISTRIBUTION;
        this.splitSparkLifeDistribution = SPLIT_SPARK_LIFE_DISTRIBUTION;
        this.sparkMassDistribution = SPARK_MASS_DISTRIBUTION;
        this.sparkTailLengthDistribution = SPARK_TAIL_LENGTH_DISTRIBUTION;
        this.sparkBirthPointDiffuseFactorDistribution = SPARK_BIRTH_POINT_DIFFUSE_FACTOR_DISTRIBUTION;
    }

    public List<SparkParticle> generateSparkParticles() {
        Vector3D birthPoint = SPARK_START_POINT.add(new Vector3D(1, 1, 0).normalize()
                .scalarMultiply(sparkBirthPointDiffuseFactorDistribution.sample()));

        List<SparkParticle> sparkParticles = new ArrayList<>();
        for (int i = 0; i < NO_OF_SPARKS_TO_GENERATE_EACH_ITERATION; i++) {
            sparkParticles.add(SparkParticle.builder()
                    .mass((float) sparkMassDistribution.sample())
                    .position(birthPoint)
                    .restitutionCoefficient(SPARK_PARTICLE_RESTITUTION_COEFFICIENT)
                    .velocity(getSparkVelocity())
                    .life((long) sparkLifeDistribution.sample())
                    .tailSize((int) sparkTailLengthDistribution.sample())
                    .createdAt(timeManager.getCurrentTimeMillis())
                    .build());
        }

        return sparkParticles;
    }

    public List<SparkParticle> generateSparkParticlesFromParent(SparkParticle sparkParticle, int noOfResultingParticles) {

        double mass = sparkParticle.getMass() / noOfResultingParticles;
        Vector3D velocityDirection = sparkParticle.getVelocity().normalize();

        // get velocities for these
        // add some perpendicular component to these velocities such that the sum of the perpendiculars is zero
        double theta = Math.toRadians(360 / noOfResultingParticles);
        Vector3D velocities[] = new Vector3D[noOfResultingParticles];
        velocities[0] = velocityDirection.crossProduct(Vector3D.PLUS_I).normalize()
                .scalarMultiply(SPARK_PARTICLE_SPLIT_SIDEWAYS_VELOCITY)
                .add(sparkParticle.getVelocity());

        for (int i = 1; i < noOfResultingParticles; i++) {
            velocities[i] = MathUtils
                    .rotateVector(velocities[i - 1], velocityDirection, Math.cos(theta), Math.sin(theta))
                    .scalarMultiply(SPARK_PARTICLE_SPLIT_SIDEWAYS_VELOCITY)
                    .add(sparkParticle.getVelocity());
        }

        List<SparkParticle> sparkParticles = new ArrayList<>();
        for (int i = 0; i < noOfResultingParticles; i++) {

            SparkParticle particle = SparkParticle.builder()
                    .mass((float) mass)
                    .position(sparkParticle.getPosition())
                    .restitutionCoefficient(SPARK_PARTICLE_RESTITUTION_COEFFICIENT)
                    .velocity(velocities[i])
                    .life((long) splitSparkLifeDistribution.sample())
                    .tailSize((int) sparkTailLengthDistribution.sample())
                    .createdAt(sparkParticle.getCreatedAt())
                    .build();

            particle.setCollided(true);

            sparkParticles.add(particle);
        }

        return sparkParticles;
    }

    private Vector3D getSparkVelocity() {
        double xCoordinate = - xForwardNormalDistribution.sample();
        double yCoordinate = yForwardNormalDistribution.sample() - 1;
        double zCoordinate = zForwardNormalDistribution.sample();

        if (xCoordinate == 0 && yCoordinate == 0 && zCoordinate == 0) {
            xCoordinate = yCoordinate = zCoordinate = 1;
        }

        Vector3D vector3d = new Vector3D(xCoordinate, yCoordinate, zCoordinate).normalize();

        vector3d = vector3d.scalarMultiply(forwardVelocityMagnitudeDistribution.sample());

        return vector3d;
    }

}
