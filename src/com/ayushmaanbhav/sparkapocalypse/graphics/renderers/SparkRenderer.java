package com.ayushmaanbhav.sparkapocalypse.graphics.renderers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.ayushmaanbhav.sparkapocalypse.config.AppConfig;
import com.ayushmaanbhav.sparkapocalypse.physics.engine.TimeManager;
import com.ayushmaanbhav.sparkapocalypse.physics.entities.SparkParticle;
import com.ayushmaanbhav.sparkapocalypse.utils.ModelLoader;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Renderer for sparks
 * @author ayush
 */
public class SparkRenderer implements Renderer<SparkParticle> {

    private Texture sparkTexture;
    private final GLU glu;
    private final GLUT glut;
    private final TimeManager timeManager;

    public SparkRenderer(GLU glu, GLUT glut, TimeManager timeManager) {
        this.glu = glu;
        this.glut = glut;
        this.timeManager = timeManager;
    }

    @Override
    public void init(GL2 gl) {
        this.sparkTexture = ModelLoader.loadTexture(AppConfig.SPARK_TEXTURE_FILE);
    }

    /**
     * Draw spark
     * @param sparkParticle - particle to draw
     * @param gl - GL2 object
     */
    @Override
    public void display(GL2 gl, SparkParticle sparkParticle) {

        gl.glPushMatrix();

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_SRC_ALPHA);

        sparkTexture.enable(gl);
        sparkTexture.bind(gl);

        double mass = sparkParticle.getMass();
        double percentageLifeElasped = (timeManager.getCurrentTimeMillis() - sparkParticle.getCreatedAt())
                / (double) sparkParticle.getLife();

        // sparks turn redder as they age
        double redStartAt = 0.6;
        gl.glTexCoord2d(1.0, percentageLifeElasped > redStartAt? redStartAt : percentageLifeElasped);

        Vector3D vertex = sparkParticle.getPosition();

        if (sparkParticle.isCollided()) {
            gl.glTranslated(vertex.getX(), vertex.getY(), vertex.getZ());;
            glut.glutSolidSphere(0.03 * mass, 3, 4);
            gl.glTranslated(-vertex.getX(), -vertex.getY(), -vertex.getZ());
        }

        gl.glTexCoord2d(1.0, percentageLifeElasped);

        gl.glBegin(GL2.GL_LINE_STRIP);

        gl.glVertex3d(vertex.getX(), vertex.getY(), vertex.getZ());

        List<Vector3D> tailVertices = new ArrayList<>(sparkParticle.getTail());

        for (int i = tailVertices.size() - 1; i >= 0; i--) {
            Vector3D tailVertex = tailVertices.get(i);
            gl.glVertex3d(tailVertex.getX(), tailVertex.getY(), tailVertex.getZ());
        }

        gl.glEnd();
        gl.glDisable(GL2.GL_BLEND);

        gl.glPopMatrix();

    }

}
