package com.ayushmaanbhav.sparkapocalypse.config;

import java.io.File;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * App config
 * @author ayush
 */
public class AppConfig {

    // Allpication root
    public static final String ROOT = System.getProperty("user.dir");

    // Resources folder
    public static final File RESOURCES_FOLDER = new File(ROOT, "resources");

    // Model files
    public static final File CONE_MODEL_FILE           = new File(RESOURCES_FOLDER, "cone.obj");
    public static final File CUBE_MODEL_FILE           = new File(RESOURCES_FOLDER, "cube.obj");
    public static final File SLAB_MODEL_FILE           = new File(RESOURCES_FOLDER, "slab.obj");
    public static final File GROUND_MODEL_FILE         = new File(RESOURCES_FOLDER, "ground.obj");
    public static final File SAWBLADE_MODEL_FILE       = new File(RESOURCES_FOLDER, "sawblade.obj");
    public static final File SPHERE_MODEL_FILE         = new File(RESOURCES_FOLDER, "sphere.obj");
    public static final File STANFORD_BUNNY_MODEL_FILE = new File(RESOURCES_FOLDER, "stanford_bunny.obj");

    // Texture files
    public static final File GROUND_TEXTURE_FILE      = new File(RESOURCES_FOLDER, "ground.jpg");
    public static final File GROUND1_TEXTURE_FILE     = new File(RESOURCES_FOLDER, "ground1.jpg");
    public static final File ROCKY_TEXTURE_FILE       = new File(RESOURCES_FOLDER, "rocky.jpg");
    public static final File GROUND_TILE_TEXTURE_FILE = new File(RESOURCES_FOLDER, "ground_tile.jpg");
    public static final File SAWBLADE_TEXTURE_FILE    = new File(RESOURCES_FOLDER, "sawblade.jpg");
    public static final File SLAB_TEXTURE_FILE        = new File(RESOURCES_FOLDER, "slab.jpg");
    public static final File WOOD_TEXTURE_FILE        = new File(RESOURCES_FOLDER, "wood.jpg");
    public static final File CONE_TEXTURE_FILE        = new File(RESOURCES_FOLDER, "cone.jpg");
    public static final File FOOTBALL_TEXTURE_FILE    = new File(RESOURCES_FOLDER, "football.jpg");
    public static final File RABBIT_TEXTURE_FILE      = new File(RESOURCES_FOLDER, "rabbit.jpg");
    public static final File WOOD2_TEXTURE_FILE       = new File(RESOURCES_FOLDER, "wood2.jpg");
    public static final File SPARK_TEXTURE_FILE       = new File(RESOURCES_FOLDER, "spark1.jpg");

    // Offsets for the model objects (offsets for their positions in the world)
    public static final Vector3D CONE_OFFSET           = new Vector3D( -2,  0,  1.0 );
    public static final Vector3D CUBE_OFFSET           = new Vector3D( -2,  0, -0.5 );
    public static final Vector3D SLAB_OFFSET           = new Vector3D(  0,  0,  0   );
    public static final Vector3D GROUND_OFFSET         = new Vector3D(  0,  0,  0   );
    public static final Vector3D SAWBLADE_OFFSET       = new Vector3D(  0,  0,  0   );
    public static final Vector3D SPHERE_OFFSET         = new Vector3D(  0,  0,  1.3 );
    public static final Vector3D STANFORD_BUNNY_OFFSET = new Vector3D( -1,  0, -1.0 );

    public static final float CONE_RESTITUTION_COEFFICIENT           = 0.8f;
    public static final float CUBE_RESTITUTION_COEFFICIENT           = 0.8f;
    public static final float SLAB_RESTITUTION_COEFFICIENT           = 0.8f;
    public static final float GROUND_RESTITUTION_COEFFICIENT         = 0.5f;
    public static final float SAWBLADE_RESTITUTION_COEFFICIENT       = 0.8f;
    public static final float SPHERE_RESTITUTION_COEFFICIENT         = 0.8f;
    public static final float STANFORD_BUNNY_RESTITUTION_COEFFICIENT = 0.5f;

    // width and height of the application window
    public static final int CANVAS_WIDTH  = 1600;
    public static final int CANVAS_HEIGHT = 900;

    // Frames FPS of the app
    public static final int REQUIRED_FPS     = 120;
    public static final int MAX_RELATIVE_FPS = 240;

    // Lighting setup
    public static final float[] AMBIENT_LIGHT   = {   0.5f,  0.5f,  0.5f,  1.0f };
    public static final float[] DIFFUSE_LIGHT   = {   0.8f,  0.8f,  0.8f,  1.0f };
    public static final float[] SPECULAR_LIGHT  = {   1.0f,  1.0f,  1.0f,  1.0f };
    public static final float[] LIGHT0_POSITION = {  15.0f, 15.0f, -15.0f, 0.0f };
    public static final float[] LIGHT1_POSITION = { -15.0f, 15.0f, -15.0f, 0.0f };
    public static final float[] LIGHT2_POSITION = {   0.0f, 15.0f,  15.0f, 0.0f };

    // Initial camera config
    public static final Vector3D CAMERA_POSITION      = new Vector3D(  3,  3, -4 );
    public static final Vector3D CAMERA_ORIENTATION   = new Vector3D(  0,  1,  0 );
    public static final Vector3D CAMERA_LOOK_AT       = new Vector3D( -1,  0,  0 );
    public static final double   CAMERA_MOVE_FACTOR   = 0.15;
    public static final double   CAMERA_ROTATE_FACTOR = 0.15;

    // Physics config like gravity, air friction, surface friction
    public static final Vector3D GRAVITY                      = new Vector3D(0, -0.0000098, 0);
    public static final double   AIR_RESISTANCE_COEFFICIENT   = 0.0000001;

    // collision algorithms constants
    public static final Vector3D COLLISION_RAY_CASTING_REFERENCE_VECTOR = new Vector3D(0, 5, 0);
    public static final double   COLLISION_DISTANCE_THRESHOLD           = 0.0002;

    // offsets for AxisAlignedBoundingBoxes
    public static final double BOUNDING_BOX_EXTRA_PRECAUTION          = 0.05;
    public static final double BOUNDING_BOX_EXTRA_PRECAUTION_SAWBLADE = 0.002;

    // Spark config
    public static final int      NO_OF_SPARKS_TO_GENERATE_EACH_ITERATION = 100;
    public static final Vector3D SPARK_START_POINT                       = new Vector3D(-0.38, 1.22, 0);
    public static final double   SPARK_START_POINT_DIFFUSE_FACTOR        = 0.02;
    public static final float    SPARK_PARTICLE_RESTITUTION_COEFFICIENT  = 1f;
    public static final float    SPARK_PARTICLE_SPLIT_PROBABLITY         = 0.2f;
    public static final int      SPARK_PARTICLE_SPLIT_MAX                = 4;
    public static final double   SPARK_PARTICLE_SPLIT_SIDEWAYS_VELOCITY  = 0.0009;

    // Spark generator config
    public static final RealDistribution X_FORWARD_NORMAL_DISTRIBUTION                 = new NormalDistribution( 6,   2   );
    public static final RealDistribution Y_FORWARD_NORMAL_DISTRIBUTION                 = new NormalDistribution( 8,   6   );
    public static final RealDistribution Z_FORWARD_NORMAL_DISTRIBUTION                 = new NormalDistribution( 0,   1.5 );
    public static final RealDistribution FORWARD_VELOCITY_MAGNITUDE_DISTRIBUTION       = new TriangularDistribution(  0.001, 0.004, 0.008 );
    public static final RealDistribution SPARK_LIFE_DISTRIBUTION                       = new TriangularDistribution(  1    , 1    , 1500  );
    public static final RealDistribution SPLIT_SPARK_LIFE_DISTRIBUTION                 = new TriangularDistribution(  1    , 1    , 250  );
    public static final RealDistribution SPARK_MASS_DISTRIBUTION                       = new TriangularDistribution(  0.1  , 0.5  , 1     );
    public static final RealDistribution SPARK_TAIL_LENGTH_DISTRIBUTION                = new TriangularDistribution(  1    , 2    , 3     );
    public static final RealDistribution SPARK_BIRTH_POINT_DIFFUSE_FACTOR_DISTRIBUTION = new TriangularDistribution( -0.02 , 0    , 0.02  );

}
