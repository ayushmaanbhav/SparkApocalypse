package com.ayushmaanbhav.sparkapocalypse.graphics.entities;

import java.util.ArrayList;
import java.util.List;

import com.ayushmaanbhav.sparkapocalypse.graphics.animations.Animation;
import com.google.common.base.Preconditions;
import com.jogamp.opengl.util.texture.Texture;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Object model
 * 
 * @author ayush
 */
@Value
@Builder
public class Model {

    private final List<Face> faces;
    private final Texture texture;

    @Wither private final List<Animation> animations;

    public Model(List<Face> faces, Texture texture, List<Animation> animations) {
        Preconditions.checkArgument(faces != null && !faces.isEmpty());
        Preconditions.checkArgument(texture != null);

        this.faces = faces;
        this.texture = texture;

        if (animations == null) {
            this.animations = new ArrayList<>();
        } else {
            this.animations = animations;
        }
    }

}
