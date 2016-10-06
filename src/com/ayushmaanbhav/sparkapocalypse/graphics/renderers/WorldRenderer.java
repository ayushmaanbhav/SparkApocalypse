package com.ayushmaanbhav.sparkapocalypse.graphics.renderers;

import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Model;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.World;
import com.ayushmaanbhav.sparkapocalypse.physics.engine.TimeManager;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.PhysicalObject;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.SparkParticle;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Renderer for world and its objects:
 * Knows about the World and its objects and which renderer to use on each.
 * 
 * @author ayush
 */
public class WorldRenderer implements Renderer<World> {

    private final Renderer<Void> lightingManager;
    private final Renderer<SparkParticle> sparkRenderer;
    private final Renderer<Model> modelRenderer;

    public WorldRenderer(GLU glu, GLUT glut, TimeManager timeManager) {
        this.lightingManager = new LightingManager();
        this.sparkRenderer = new SparkRenderer(glu, glut, timeManager);
        this.modelRenderer = new ModelRenderer();
    }

    /**
     * Initialize the world render system
     * @param gl - GL2
     */
    @Override
    public void init(GL2 gl) {
        gl.glClearColor(0, 0, 0, 0);

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glShadeModel(GL2.GL_SMOOTH);

        lightingManager.init(gl);

        sparkRenderer.init(gl);
        modelRenderer.init(gl);
    }

    /**
     * Display the world and its objects
     * @param world - World to display
     * @param gl - GL2 object
     * @param glu - GLU object
     */
    @Override
    public void display(GL2 gl, World world) {

        for (PhysicalObject physicalObject : world.getPhysicalObjects()) {
            modelRenderer.display(gl, physicalObject.getModel());
        }

        for (SparkParticle sparkParticle : world.getSparkParticles()) {
            sparkRenderer.display(gl, sparkParticle);
        }
    }

}
