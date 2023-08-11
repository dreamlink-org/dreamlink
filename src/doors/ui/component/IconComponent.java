package doors.ui.component;

import doors.graphics.spritebatch.SpriteBatch;
import doors.graphics.texture.sample.TextureSample;
import doors.ui.component.layout.box.BoxComponent;
import doors.ui.component.layout.box.FixedDimension;
import doors.ui.root.UIRoot;
import doors.utility.vector.Vector2in;

public class IconComponent implements IComponent {

    private BackgroundComponent backgroundComponent;
    private BoxComponent spaceComponent;

    public IconComponent(TextureSample textureSample, Vector2in dimensions) {
        this.spaceComponent = new BoxComponent(
            null,
            new FixedDimension(dimensions.x),
            new FixedDimension(dimensions.y)
        );

        this.backgroundComponent = new BackgroundComponent(this.spaceComponent, textureSample);
    }

    public void setTextureSample(TextureSample textureSample) {
        this.backgroundComponent.textureSample = textureSample;
    }

    public IconComponent(TextureSample textureSample) {
        this(textureSample, textureSample.dimensions);
    }

    @Override
    public Vector2in getDimensions() {
        return this.backgroundComponent.getDimensions();
    }

    @Override
    public Vector2in getPosition() {
        return this.backgroundComponent.getPosition();
    }

    @Override
    public void calculateDimensions() {
        this.backgroundComponent.calculateDimensions();
    }

    @Override
    public void adjustDimensions(Vector2in availableSpace) {
        this.backgroundComponent.adjustDimensions(availableSpace);
    }

    @Override
    public void calculatePosition(Vector2in origin) {
        this.backgroundComponent.calculatePosition(origin);
    }

    @Override
    public void update(UIRoot root) {
        this.backgroundComponent.update(root);
    }

    @Override
    public void writeComponentToSpriteBatch(SpriteBatch spriteBatch) {
        this.backgroundComponent.writeComponentToSpriteBatch(spriteBatch);
    }

    
}
