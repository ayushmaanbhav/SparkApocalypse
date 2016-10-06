package com.ayushmaanbhav.sparkapocalypse;

import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.MAX_RELATIVE_FPS;
import static com.ayushmaanbhav.sparkapocalypse.config.AppConfig.REQUIRED_FPS;

import java.io.IOException;

import javax.swing.JFrame;

import com.ayushmaanbhav.sparkapocalypse.config.AppConfig;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.World;
import com.ayushmaanbhav.sparkapocalypse.graphics.renderers.WorldRenderer;
import com.ayushmaanbhav.sparkapocalypse.io.CameraController;
import com.ayushmaanbhav.sparkapocalypse.physics.engine.PhysicsEngine;
import com.ayushmaanbhav.sparkapocalypse.physics.engine.TimeManager;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import lombok.Getter;

/**
 * Spark Apocalypse
 * 
 * An app which implements various concepts of maths, physics and rendering along with head humming creavity
 * to show a beautiful world containing sparks and er... some other objects like a rabbit for example ^_^
 * 
 * @author ayush
 */
public class Main implements GLEventListener {

    private final GLU glu;
    private final GLUT glut;

    private final World world;
    private final WorldRenderer worldRenderer;
    private final PhysicsEngine physicsEngine;
    @Getter
    private final CameraController cameraController;
    private final TimeManager timeManager;

    public Main() {
        this.glu = new GLU();
        this.glut = new GLUT();

        this.world = new World();
        this.timeManager = new TimeManager(REQUIRED_FPS / (double) MAX_RELATIVE_FPS);
        this.cameraController = new CameraController();
        this.worldRenderer = new WorldRenderer(glu, glut, timeManager);
        this.physicsEngine = new PhysicsEngine(timeManager);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        cameraController.setupCamera(gl, glu);

        gl.glPushMatrix();
        //long start = System.currentTimeMillis();
        worldRenderer.display(gl, world);
        //long timeTaken = System.currentTimeMillis() - start;
        //System.out.println("Render time: " + timeTaken);
        gl.glPopMatrix();

        gl.glFlush();

        //start = System.currentTimeMillis();
        physicsEngine.run(world);
        //timeTaken = System.currentTimeMillis() - start;
        //System.out.println("Physics time: " + timeTaken);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        drawable.getAnimator().setUpdateFPSFrames(30, System.out);
        GL2 gl = drawable.getGL().getGL2();

        try {
            world.init();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred during loading models", e);
        }        
        worldRenderer.init(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height <= 0)
            height = 1;

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(45.0, width / (double) height, 0.1, 100);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        cameraController.setupCamera(gl, glu);
    }

    public static void main(String[] args) {

        Main application = new Main();

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas glcanvas = new GLCanvas(capabilities);

        glcanvas.addGLEventListener(application);
        glcanvas.setSize(AppConfig.CANVAS_WIDTH, AppConfig.CANVAS_HEIGHT);

        glcanvas.addKeyListener(application.getCameraController());
        glcanvas.addMouseMotionListener(application.getCameraController());

        JFrame frame = new JFrame("Spark Apocalypse");

        frame.getContentPane().add(glcanvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(glcanvas, AppConfig.REQUIRED_FPS, true);
        animator.start();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

}