package dreamlink.menu.component.core.box;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.menu.component.core.BaseMenuComponent;

public class BoxComponent extends BaseMenuComponent {

    private Vector2i initialDimensions;
    private Vector2i dimensions;

    private BoxDimension width;
    private BoxDimension height;

    private BaseMenuComponent component;

    public BoxComponent(
        BaseMenuComponent component, 
        BoxDimension width, 
        BoxDimension height
    ) {
        this.initialDimensions = new Vector2i();
        this.dimensions = new Vector2i();
        this.width = width;
        this.height = height;
        this.component = component;
    }

    @Override
    public Vector2i getInitialDimensions() {
        return this.initialDimensions;
    }

    @Override
    public Vector2i getDimensions() {
        return this.dimensions;
    }

    @Override
    public void computeInitialDimensions() {
        this.component.computeInitialDimensions();
        var componentInitialDimensions = this.component.getInitialDimensions();
        this.initialDimensions.set(
            Math.max(this.width.minValue, componentInitialDimensions.x),
            Math.max(this.height.minValue, componentInitialDimensions.y)
        );
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        this.dimensions.set(
            Math.max(this.initialDimensions.x, Math.min(this.width.maxValue, availableSpace.x)),
            Math.max(this.initialDimensions.y, Math.min(this.height.maxValue, availableSpace.y))
        );
        this.component.computeDimensions(this.dimensions);
    }

    @Override
    public void finalizeLayout(BaseMenuComponent parent, Vector2i position) {
        super.finalizeLayout(parent, position);

        var innerDimensions = this.component.getDimensions();
        var adjustedPosition = new Vector2i(
            position.x + this.width.alignment.getOffset(this.dimensions.x, innerDimensions.x),
            position.y + this.height.alignment.getOffset(this.dimensions.y, innerDimensions.y)
        );
        this.component.finalizeLayout(this, adjustedPosition);
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        return this.component.getHoveredComponent();
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        this.component.writeToSpriteBatch(spriteBatch);
    }
    
}
