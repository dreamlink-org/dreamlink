package dreamlink.menu.component.core;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;

public class PaddingComponent extends BaseMenuComponent {

    public int paddingTop;
    public int paddingBottom;
    public int paddingLeft;
    public int paddingRight;

    private Vector2i initialDimensions = new Vector2i();
    private Vector2i dimensions = new Vector2i();
    private BaseMenuComponent component;

    public PaddingComponent(BaseMenuComponent component, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
        this.component = component;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
    }

    public PaddingComponent(BaseMenuComponent content, int padding) {
        this(content, padding, padding, padding, padding);
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
        var horizontalPadding = this.paddingLeft + this.paddingRight;
        var verticalPadding = this.paddingTop + this.paddingBottom;

        this.component.computeInitialDimensions();
        this.initialDimensions.set(this.component.getInitialDimensions());
        this.initialDimensions.add(horizontalPadding, verticalPadding);
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        var horizontalPadding = this.paddingLeft + this.paddingRight;
        var verticalPadding = this.paddingTop + this.paddingBottom;

        var innerDimensions = new Vector2i(availableSpace).sub(horizontalPadding, verticalPadding);
        this.component.computeDimensions(innerDimensions);

        this.dimensions.set(this.component.getDimensions());
        this.dimensions.add(horizontalPadding, verticalPadding);
    }

    @Override
    public void finalizeLayout(BaseMenuComponent parent, Vector2i position) {
        super.finalizeLayout(parent, position);
        var innerPosition = new Vector2i(
            position.x + this.paddingLeft,
            position.y + this.paddingTop
        );
        this.component.finalizeLayout(this, innerPosition);
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
