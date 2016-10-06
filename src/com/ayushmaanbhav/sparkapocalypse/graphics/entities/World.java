package com.ayushmaanbhav.sparkapocalypse.graphics.entities;

import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.BOUNDING_BOX_EXTRA_PRECAUTION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.BOUNDING_BOX_EXTRA_PRECAUTION_SAWBLADE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.CONE_MODEL_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.CONE_OFFSET;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.CONE_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.CONE_TEXTURE_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.CUBE_MODEL_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.CUBE_OFFSET;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.CUBE_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.FOOTBALL_TEXTURE_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.GROUND_MODEL_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.GROUND_OFFSET;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.GROUND_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.GROUND_TILE_TEXTURE_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.RABBIT_TEXTURE_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SAWBLADE_MODEL_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SAWBLADE_OFFSET;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SAWBLADE_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SAWBLADE_TEXTURE_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SLAB_MODEL_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SLAB_OFFSET;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SLAB_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SLAB_TEXTURE_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPHERE_MODEL_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPHERE_OFFSET;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPHERE_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.STANFORD_BUNNY_MODEL_FILE;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.STANFORD_BUNNY_OFFSET;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.STANFORD_BUNNY_RESTITUTION_COEFFICIENT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.WOOD2_TEXTURE_FILE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.ayushmaanbhav.sparkapocalypse.graphics.animations.RotationAnimation;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.PhysicalObject;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.SparkParticle;
import com.ayushmaanbhav.sparkapocalypse.utils.ModelLoader;

import lombok.Value;

/**
 * The physical world
 * 
 * @author ayush
 */
@Value
public class World {

    private final List<PhysicalObject> physicalObjects;
    private final List<SparkParticle> sparkParticles;

    public World() {
        sparkParticles = new ArrayList<>(10000);
        physicalObjects = new ArrayList<>(10);
    }

    public void init() throws IOException {

        // load models and offsets
        Model coneModel = ModelLoader.loadModel(CONE_MODEL_FILE, CONE_TEXTURE_FILE, CONE_OFFSET);
        Model cubeModel = ModelLoader.loadModel(CUBE_MODEL_FILE, WOOD2_TEXTURE_FILE, CUBE_OFFSET);
        Model groundModel = ModelLoader.loadModel(GROUND_MODEL_FILE, GROUND_TILE_TEXTURE_FILE, GROUND_OFFSET);
        Model sawbladeModel = ModelLoader.loadModel(SAWBLADE_MODEL_FILE, SAWBLADE_TEXTURE_FILE, SAWBLADE_OFFSET);
        Model slabModel = ModelLoader.loadModel(SLAB_MODEL_FILE, SLAB_TEXTURE_FILE, SLAB_OFFSET);
        Model sphereModel = ModelLoader.loadModel(SPHERE_MODEL_FILE, FOOTBALL_TEXTURE_FILE, SPHERE_OFFSET);
        Model stanfordBunnyModel = ModelLoader.loadModel(STANFORD_BUNNY_MODEL_FILE, RABBIT_TEXTURE_FILE,
                STANFORD_BUNNY_OFFSET);

        // set animations
        sawbladeModel = sawbladeModel.withAnimations(Arrays.asList(new RotationAnimation(sawbladeModel)));

        // poulate physical objects
        physicalObjects.add(new PhysicalObject(coneModel, CONE_RESTITUTION_COEFFICIENT, BOUNDING_BOX_EXTRA_PRECAUTION));
        physicalObjects.add(new PhysicalObject(cubeModel, CUBE_RESTITUTION_COEFFICIENT, BOUNDING_BOX_EXTRA_PRECAUTION));
        physicalObjects
                .add(new PhysicalObject(groundModel, GROUND_RESTITUTION_COEFFICIENT, BOUNDING_BOX_EXTRA_PRECAUTION));
        physicalObjects.add(new PhysicalObject(sawbladeModel, SAWBLADE_RESTITUTION_COEFFICIENT,
                BOUNDING_BOX_EXTRA_PRECAUTION_SAWBLADE));
        physicalObjects.add(new PhysicalObject(slabModel, SLAB_RESTITUTION_COEFFICIENT, BOUNDING_BOX_EXTRA_PRECAUTION));
        physicalObjects
                .add(new PhysicalObject(sphereModel, SPHERE_RESTITUTION_COEFFICIENT, BOUNDING_BOX_EXTRA_PRECAUTION));
        physicalObjects.add(new PhysicalObject(stanfordBunnyModel, STANFORD_BUNNY_RESTITUTION_COEFFICIENT,
                BOUNDING_BOX_EXTRA_PRECAUTION));

    }

    public void addSparkParticles(Collection<SparkParticle> sparkParticles) {
        this.sparkParticles.addAll(sparkParticles);
    }

    public void remove(List<SparkParticle> sparkParticlesToDestroy) {
        this.sparkParticles.removeAll(sparkParticlesToDestroy);
    }

}
