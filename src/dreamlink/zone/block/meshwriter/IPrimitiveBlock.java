package dreamlink.zone.block.meshwriter;

import org.joml.Vector3i;

import dreamlink.graphics.texture.sample.TextureSample;
import dreamlink.utility.maths.CubeFace;
import dreamlink.zone.block.IBlock;

public interface IPrimitiveBlock extends IBlock {

    public boolean isTransparent();

    public boolean isHidden();

    public boolean isAffectedByLight();

    public TextureSample getTextureSample(Vector3i position, CubeFace cubeFace);
    
}
