package dreamlink.menu.component.core.span;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.utility.maths.Vector2iMaths;

public class SpanComponent extends BaseMenuComponent {

    private List<BaseMenuComponent> components;
    private SpanOrientation orientation;
    private Vector2i initialDimensions;
    private Vector2i dimensions;
    private int spacing;
    private SpanAlignment alignment = SpanAlignment.center;

    public SpanComponent(SpanOrientation orientation, SpanAlignment alignment, int spacing) {
        this.orientation = orientation;
        this.alignment = alignment;
        this.spacing = spacing;
        this.components = new ArrayList<>();
        this.initialDimensions = new Vector2i();
        this.dimensions = new Vector2i();
    }

    public SpanComponent addComponent(BaseMenuComponent component) {
        this.components.add(component);
        return this;
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
        var primaryDimensionSpacing = this.spacing * Math.max(this.components.size() - 1, 0);
        var totalPrimaryDimensionConsumed = 0;
        var totalSecondaryDimensionConsumed = 0;

        for(var component : this.components) {
            component.computeInitialDimensions();
            var componentDimensions = component.getInitialDimensions();
            totalPrimaryDimensionConsumed += Vector2iMaths.getAxisValue(componentDimensions, orientation.primaryAxis);
            totalSecondaryDimensionConsumed = Math.max(totalSecondaryDimensionConsumed, Vector2iMaths.getAxisValue(componentDimensions, orientation.secondaryAxis));
        }

        Vector2iMaths.setAxisValue(this.initialDimensions, orientation.primaryAxis, totalPrimaryDimensionConsumed + primaryDimensionSpacing);
        Vector2iMaths.setAxisValue(this.initialDimensions, orientation.secondaryAxis, totalSecondaryDimensionConsumed);
    }

    @Override
    public void computeDimensions(Vector2i availableSpace) {
        var availablePrimaryDimension = Vector2iMaths.getAxisValue(availableSpace, orientation.primaryAxis);
        var availableSecondaryDimension = Vector2iMaths.getAxisValue(availableSpace, orientation.secondaryAxis);
        var availableExtraPrimaryDimension = availablePrimaryDimension - Vector2iMaths.getAxisValue(this.initialDimensions, orientation.primaryAxis);

        var requestedExtraPrimaryDimension = 0;
        var requestedSecondaryDimension = 0;

        var componentAvailableSpace = new Vector2i();
        Vector2iMaths.setAxisValue(componentAvailableSpace, orientation.secondaryAxis, availableSecondaryDimension);

        for(var component : this.components) {
            var componentInitialDimensions = component.getInitialDimensions();
            var componentInitialPrimaryDimension = Vector2iMaths.getAxisValue(componentInitialDimensions, orientation.primaryAxis);
            Vector2iMaths.setAxisValue(componentAvailableSpace, orientation.primaryAxis, componentInitialPrimaryDimension + availableExtraPrimaryDimension);
            component.computeDimensions(componentAvailableSpace);

            var componentDimensions = component.getDimensions();
            var componentPrimaryDimension = Vector2iMaths.getAxisValue(componentDimensions, orientation.primaryAxis);
            requestedExtraPrimaryDimension += componentPrimaryDimension - componentInitialPrimaryDimension;
            requestedSecondaryDimension = Math.max(requestedSecondaryDimension, Vector2iMaths.getAxisValue(componentDimensions, orientation.secondaryAxis));
        }

        var firstPassRequestedExtraPrimaryDimension = requestedExtraPrimaryDimension;

        if(requestedExtraPrimaryDimension > availableExtraPrimaryDimension) {
            requestedExtraPrimaryDimension = 0;
            requestedSecondaryDimension = 0;

            for(var ix = 0; ix < this.components.size(); ix += 1) {
                var component = this.components.get(ix);
                var componentDimensions = component.getDimensions();
                var componentInitialDimensions = component.getInitialDimensions();

                var componentInitialPrimaryDimension = Vector2iMaths.getAxisValue(componentInitialDimensions, orientation.primaryAxis);
                var componentPrimaryDimension = Vector2iMaths.getAxisValue(componentDimensions, orientation.primaryAxis);
                var componentRequestedExtraPrimaryDimension = componentPrimaryDimension - componentInitialPrimaryDimension;

                var proportion = (float) componentRequestedExtraPrimaryDimension / firstPassRequestedExtraPrimaryDimension;
                var componentAvailableExtraPrimaryDimension = ix == this.components.size() - 1
                    ? availableExtraPrimaryDimension - requestedExtraPrimaryDimension
                    : (int)(availableExtraPrimaryDimension * proportion);

                Vector2iMaths.setAxisValue(componentAvailableSpace, orientation.primaryAxis, componentInitialPrimaryDimension + componentAvailableExtraPrimaryDimension);
                component.computeDimensions(componentAvailableSpace);

                var scaledComponentDimensions = component.getDimensions();
                var scaledComponentPrimaryDimension = Vector2iMaths.getAxisValue(scaledComponentDimensions, orientation.primaryAxis);

                requestedExtraPrimaryDimension += scaledComponentPrimaryDimension - componentInitialPrimaryDimension;
                requestedSecondaryDimension = Math.max(requestedSecondaryDimension, Vector2iMaths.getAxisValue(scaledComponentDimensions, orientation.secondaryAxis));
            }
        }

        Vector2iMaths.setAxisValue(this.dimensions, orientation.primaryAxis, requestedExtraPrimaryDimension + Vector2iMaths.getAxisValue(this.initialDimensions, orientation.primaryAxis));
        Vector2iMaths.setAxisValue(this.dimensions, orientation.secondaryAxis, requestedSecondaryDimension);

    }


    @Override
    public void finalizeLayout(BaseMenuComponent parent, Vector2i position) {
        super.finalizeLayout(parent, position);
        var innerPosition = new Vector2i(position);

        var dimensions = this.getDimensions();
        var secondaryPosition = Vector2iMaths.getAxisValue(position, orientation.secondaryAxis);
        var secondaryDimension = Vector2iMaths.getAxisValue(dimensions, orientation.secondaryAxis);

        for(var component : this.components) {
            var innerPrimaryDimension = Vector2iMaths.getAxisValue(innerPosition, orientation.primaryAxis);

            var componentDimensions = component.getDimensions();
            var componentPrimaryDimension = Vector2iMaths.getAxisValue(componentDimensions, orientation.primaryAxis); 
            var componentSecondaryDimension = Vector2iMaths.getAxisValue(componentDimensions, orientation.secondaryAxis);

            Vector2iMaths.setAxisValue(
                innerPosition,
                orientation.secondaryAxis,
                this.alignment.getOffset(
                    secondaryDimension,
                    componentSecondaryDimension
                ) + secondaryPosition
            );
            component.finalizeLayout(this, innerPosition);


            Vector2iMaths.setAxisValue(innerPosition, orientation.primaryAxis, innerPrimaryDimension + componentPrimaryDimension + this.spacing);
        }
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        for(var component : this.components) {
            var candidate = component.getHoveredComponent();
            if(candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        for(var component : this.components) {
            component.writeToSpriteBatch(spriteBatch);
        }
    }

    
}
