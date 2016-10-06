package com.ayushmaanbhav.sparkapocalypse.physics.engine;

import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.AIR_RESISTANCE_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.GRAVITY;

import java.util.ListIterator;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.ayushmaanbhav.sparkapocalypse.graphics.entities.World;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.SparkParticle;

/**
 * Physics engine for the World
 * @author ayush
 */
public class PhysicsEngine {

    private final SparkGenerator sparkGenerator;
    private final CollisionDectector collisionDectector;
    private final CollisionHandler collisionHandler;
    private final TimeManager timeManager;

    public PhysicsEngine(TimeManager timeManager) {
        this.timeManager = timeManager;
        this.sparkGenerator = new SparkGenerator(timeManager);
        this.collisionHandler = new CollisionHandler(sparkGenerator);
        this.collisionDectector = new CollisionDectector(collisionHandler);
    }

    /**
     * Run physics engine on the given world or spark particles
     * Apply rules like gravity etc
     * @param world - World
     */
    public void run(World world) {
        timeManager.updateTime();

        generateAndDestroySparkParticles(world);

        for (SparkParticle sparkParticle : world.getSparkParticles()) {
            computeNextPosition(sparkParticle);
            computeNextVelocity(sparkParticle);

            // this will help collision engine detect possible collisions
            computeNextToNextPossiblePosition(sparkParticle);

            sparkParticle.setCollided(false);
        }

        collisionDectector.run(world);
    }

    private void generateAndDestroySparkParticles(World world) {
        // destory old spark particles

        ListIterator<SparkParticle> sparkParticleIterator = world.getSparkParticles().listIterator();
        while (sparkParticleIterator.hasNext()) {
            SparkParticle sparkParticle = sparkParticleIterator.next();

            if (timeManager.getCurrentTimeMillis() - sparkParticle.getCreatedAt() > sparkParticle.getLife()) {
                sparkParticleIterator.remove();
            }
        }

        // create new ones
        world.addSparkParticles(sparkGenerator.generateSparkParticles());
    }

    private void computeNextPosition(SparkParticle sparkParticle) {
        long timeElasped = timeManager.getElaspedTime();

        sparkParticle.setPosition(getNextPosition(sparkParticle, timeElasped));
    }

    private void computeNextToNextPossiblePosition(SparkParticle sparkParticle) {
        long timeElasped = timeManager.getAverageTimeInterval();

        sparkParticle.setPossibleNextPosition(getNextPosition(sparkParticle, timeElasped));
    }

    /**
     * v = u + at
     * @param accelerationOnTheParticle 
     */
    private void computeNextVelocity(SparkParticle sparkParticle) {
        long timeElasped =  timeManager.getElaspedTime();

        Vector3D accelerationOnTheParticle = getAccelerationOnTheParticle(sparkParticle);

        Vector3D newVelocity = sparkParticle.getVelocity()
                .add(accelerationOnTheParticle
                        .scalarMultiply(timeElasped));

        sparkParticle.setVelocity(newVelocity);
    }

    /**
     *  s = ut + 1/2at^2
     */
    private Vector3D getNextPosition(SparkParticle sparkParticle, long timeElasped) {

        Vector3D accelerationOnTheParticle = getAccelerationOnTheParticle(sparkParticle);

        return sparkParticle.getPosition()
                .add(sparkParticle.getVelocity()
                        .scalarMultiply(timeElasped))
                .add(accelerationOnTheParticle
                        .scalarMultiply(timeElasped * timeElasped * 0.5));
    }

    /**
     * Force due to GRAVITY, AIR FRICTION, GROUND FRICTION etc
     */
    private Vector3D getAccelerationOnTheParticle(SparkParticle sparkParticle) {
        // gravity
        Vector3D totalAcceleration = GRAVITY;

        // air friction/resistance : for small particles the air resistancce is proportional to the velocity of the particle
        // F = -bV, where b is a constant
        // Source: http://hyperphysics.phy-astr.gsu.edu/hbase/airfri.html
        Vector3D accelerationDueToAirResistance = sparkParticle.getVelocity()
                .scalarMultiply(- AIR_RESISTANCE_COEFFICIENT / sparkParticle.getMass());

        totalAcceleration = totalAcceleration.add(accelerationDueToAirResistance);

        return totalAcceleration;
    }

}
