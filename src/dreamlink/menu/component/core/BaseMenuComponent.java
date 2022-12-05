package dreamlink.menu.component.core;

import dreamlink.window.Window;

import org.joml.Rectanglei;
import org.joml.Vector2i;

import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.menu.event.DragEndEvent;
import dreamlink.menu.event.DragStartEvent;
import dreamlink.menu.event.HoverEndEvent;
import dreamlink.menu.event.HoverStartEvent;
import dreamlink.menu.event.IEvent;
import dreamlink.menu.event.PressEndEvent;
import dreamlink.menu.event.PressStartEvent;

public abstract class BaseMenuComponent {

    private BaseMenuComponent parent;
    private Vector2i position;

    private boolean isHovered;
    private boolean isFocused;
    private boolean isPressed;
    private boolean isDragged;

    public BaseMenuComponent getParent() {
        return this.parent;
    }

    public abstract Vector2i getInitialDimensions();

    public abstract Vector2i getDimensions();

    public Vector2i getPosition() {
        return this.position;
    }

    public void computeInitialDimensions() {

    }

    public void computeDimensions(Vector2i availableSpace) {

    }

    public void finalizeLayout(BaseMenuComponent parent, Vector2i position) {
        this.parent = parent;
        this.position = new Vector2i(position);
    }

    public boolean isHovered() {
        return this.isHovered;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public boolean isPressed() {
        return this.isPressed;
    }

    public boolean isDragged() {
        return this.isDragged;
    }

    public boolean isMouseOver() {
        var position = this.getPosition();
        var dimensions = this.getDimensions();
        return new Rectanglei(
            position.x,
            position.y,
            position.x + dimensions.x,
            position.y + dimensions.y
        ).containsPoint(Window.instance.getMousePosition());
    }

    public BaseMenuComponent getHoveredComponent() {
        return null;
    }

    public int getDragDistanceThreshold() {
        return Integer.MAX_VALUE;
    }

    public void onEvent(IEvent event) {
        if(event instanceof HoverStartEvent) {
            this.isHovered = true;
        } else if(event instanceof HoverEndEvent) {
            this.isHovered = false;
        } else if(event instanceof PressStartEvent) {
            this.isPressed = true;
        } else if(event instanceof PressEndEvent) {
            this.isPressed = false;
        } else if(event instanceof DragStartEvent) {
            this.isDragged = true;
        } else if(event instanceof DragEndEvent) {
            this.isDragged = false;
        }
    }

    public void writeToSpriteBatch(SpriteBatch spriteBatch) {

    }

}
