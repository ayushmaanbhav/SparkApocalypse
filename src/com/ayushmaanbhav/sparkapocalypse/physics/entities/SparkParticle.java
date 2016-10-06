package com.ayushmaanbhav.sparkapocalypse.physics.entities;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.google.common.collect.EvictingQueue;

import lombok.Builder;
import lombok.Data;

/**
 * Particle
 * @author ayush
 */
@Data
public class SparkParticle {

    // Physics attributes
    private Vector3D position;
    private Vector3D velocity;
    private float mass;

    private Vector3D possibleNextPosition;
    private final float restitutionCoefficient;
    private boolean collided;

    private long life;
    private long createdAt;

    private final EvictingQueue<Vector3D> tail;

    @Builder
    public SparkParticle(Vector3D position, Vector3D velocity, float mass, float restitutionCoefficient, long life, int tailSize, long createdAt) {
        this.position = position;
        this.possibleNextPosition = position;
        this.velocity = velocity;
        this.mass = mass;
        this.restitutionCoefficient = restitutionCoefficient;
        this.life = life;
        this.tail = EvictingQueue.create(tailSize);
        this.createdAt = createdAt;
        this.collided = false;
    }

    public void setPosition(Vector3D position) {
        tail.offer(this.position);
        this.position = position;
    }

}
