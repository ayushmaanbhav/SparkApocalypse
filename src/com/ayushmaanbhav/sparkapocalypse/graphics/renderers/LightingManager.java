package com.ayushmaanbhav.sparkapocalypse.graphics.renderers;

import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.AMBIENT_LIGHT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.DIFFUSE_LIGHT;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.LIGHT0_POSITION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.LIGHT1_POSITION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.LIGHT2_POSITION;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.SPECULAR_LIGHT;

import com.jogamp.opengl.GL2;

/**
 * Manager for Lights
 * @author ayush
 */
public class LightingManager implements Renderer<Void> {

    /**
     * Setup lighting
     * @param gl - GL2
     */
    @Override
    public void init(GL2 gl) {

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, AMBIENT_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, DIFFUSE_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, SPECULAR_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, LIGHT0_POSITION, 0);
        gl.glEnable(GL2.GL_LIGHT0);

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, AMBIENT_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, DIFFUSE_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, SPECULAR_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, LIGHT1_POSITION, 0);
        gl.glEnable(GL2.GL_LIGHT1);

        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, AMBIENT_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, DIFFUSE_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, SPECULAR_LIGHT, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, LIGHT2_POSITION, 0);
        gl.glEnable(GL2.GL_LIGHT2);

    }

    @Override
    public void display(GL2 gl, Void objectToDisplay) {        
    }

}
