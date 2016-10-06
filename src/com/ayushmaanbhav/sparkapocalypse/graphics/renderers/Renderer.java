package com.ayushmaanbhav.sparkapocalypse.graphics.renderers;

import com.jogamp.opengl.GL2;

/**
 * Renderer interface: Knows about how to render the T entity
 * 
 * @author ayush
 *
 * @param <T> displayable object/s
 */
public interface Renderer<T> {

    /**
     * Initialize, Load resources etc
     * Its here because some functions require GL context like texture loader
     */
    void init(GL2 gl);

    /**
     * Display object
     * @param gl - GL2 object
     * @param objectToDisplay - Object to display
     */
    void display(GL2 gl, T objectToDisplay);

}
