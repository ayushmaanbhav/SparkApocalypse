package com.ayushmaanbhav.sparkapocalypse.graphics.renderers;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.ayushmaanbhav.sparkapocalypse.graphics.animations.Animation;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Face;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Model;
import com.jogamp.opengl.GL2;

/**
 * Used to draw object model
 * 
 * @author ayush
 */
public class ModelRenderer implements Renderer<Model> {

    @Override
    public void init(GL2 gl) {
    }

    @Override
    public void display(GL2 gl, Model model) {

        gl.glPushMatrix();

        model.getTexture().enable(gl);
        model.getTexture().bind(gl);

        for (Animation animation : model.getAnimations()) {
            animation.apply(gl);
        }

        for (Face face : model.getFaces()) {
            gl.glBegin(GL2.GL_POLYGON);

            for (int i = 0; i < face.getNoOfVertices(); i++) {
                if (face.getVertexNormals().isPresent()) {
                    Vector3D vertexNormal = face.getVertexNormals().get()[i];
                    gl.glNormal3d(vertexNormal.getX(), vertexNormal.getY(), vertexNormal.getZ());
                }

                Vector2D vertexTexture = face.getVertexTextures()[i];
                gl.glTexCoord2d(vertexTexture.getX(), vertexTexture.getY());

                Vector3D vertex = face.getVertices()[i];
                gl.glVertex3d(vertex.getX(), vertex.getY(), vertex.getZ());
            }

            gl.glEnd();
        }

        gl.glPopMatrix();
    }

}
