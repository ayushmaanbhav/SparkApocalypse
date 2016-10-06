package com.ayushmaanbhav.sparkapocalypse.graphics.animations;

import com.jogamp.opengl.GL2;

/**
 * Animation interface
 * The difference between animations and renderer is that an animation is bound to its model
 * 
 * @author ayush
 */
public interface Animation {

    /**
     * Render the animation
     * @param gl - GL context
     */
    void apply(GL2 gl);

}
