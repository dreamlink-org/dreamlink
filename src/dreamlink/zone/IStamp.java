package dreamlink.zone;

import org.joml.Vector3i;

import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.utility.maths.Orientation;

public interface IStamp {

    public String getStampName();

    public String getStampType();

    public ISpriteTemplate getStampSprite();

    public void applyStamp(
        Vector3i position, 
        Orientation orientation
    );

}
