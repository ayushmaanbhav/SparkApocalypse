package com.ayushmaanbhav.sparkapocalypse.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Face;
import com.ayushmaanbhav.sparkapocalypse.graphics.entities.Model;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * Model loader for .OBJ files with textures
 * @author ayush
 */
public class ModelLoader {

    public static Model loadModel(File objFile, File textureFile, Vector3D offset) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(objFile));

        List<Vector3D> vertices = new ArrayList<>();
        List<Vector2D> vertexTextures = new ArrayList<>();
        List<Vector3D> vertexNormals = new ArrayList<>();
        List<Face> faces = new ArrayList<Face>();

        String line = null;
        while ((line = reader.readLine()) != null) {

            StringTokenizer token = new StringTokenizer(line);
            String cmd = token.nextToken();

            if (cmd.equals("v")) {
                vertices.add(read3DPoint(token).add(offset));
            } else if (cmd.equals("vn")) {
                vertexNormals.add(read3DPoint(token));
            } else if (cmd.equals("vt")) {
                vertexTextures.add(read2DPoint(token));
            } else if (cmd.equals("f")) {
                Face face = new Face(token.countTokens());

                while (token.hasMoreTokens()) {
                    StringTokenizer faceToken = new StringTokenizer(token.nextToken(), "/");

                    int vertexId = -1;
                    int vertexTextureId = -1;
                    int vertexNormalId = -1;
                    vertexId = Integer.parseInt(faceToken.nextToken());
                    if (faceToken.hasMoreTokens())
                        vertexTextureId = Integer.parseInt(faceToken.nextToken());
                    if (faceToken.hasMoreTokens())
                        vertexNormalId = Integer.parseInt(faceToken.nextToken());

                    face.addVertex(vertices.get(vertexId - 1),
                            vertexTextureId == -1 ? null : vertexTextures.get(vertexTextureId - 1),
                            vertexNormalId == -1 ? null : vertexNormals.get(vertexNormalId - 1));
                }

                faces.add(face);
            }
        }

        reader.close();

        return Model.builder()
                .faces(faces)
                .texture(loadTexture(textureFile))
                .build();
    }

    private static Vector3D read3DPoint(StringTokenizer tok) {
        return new Vector3D(Float.parseFloat(tok.nextToken()),
                Float.parseFloat(tok.nextToken()),
                Float.parseFloat(tok.nextToken()));
    }

    private static Vector2D read2DPoint(StringTokenizer tok) {
        return new Vector2D(Float.parseFloat(tok.nextToken()),
                Float.parseFloat(tok.nextToken()));
    }

    public static Texture loadTexture(File file) {
        try {
            return TextureIO.newTexture(file, true);
        } catch (GLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
