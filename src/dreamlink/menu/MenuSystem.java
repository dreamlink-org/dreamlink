package dreamlink.menu;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import dreamlink.Config;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.ZoneStatus;
import dreamlink.graphics.glstate.TextureState;
import dreamlink.graphics.glstate.blend.BlendMode;
import dreamlink.graphics.glstate.blend.BlendState;
import dreamlink.graphics.mesh.strategy.texture.MeshTextureQuadStrategy;
import dreamlink.graphics.program.SpriteShaderProgram;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.texture.EntityTexture;
import dreamlink.graphics.texture.OverlayTexture;
import dreamlink.graphics.texture.Texture;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.event.ClickEvent;
import dreamlink.menu.event.DragEndEvent;
import dreamlink.menu.event.DragEvent;
import dreamlink.menu.event.DragStartEvent;
import dreamlink.menu.event.HoverEndEvent;
import dreamlink.menu.event.HoverEvent;
import dreamlink.menu.event.HoverStartEvent;
import dreamlink.menu.event.PressEndEvent;
import dreamlink.menu.event.PressEvent;
import dreamlink.menu.event.PressStartEvent;
import dreamlink.menu.event.DropEvent;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.Vector2iMaths;

public class MenuSystem {

    public static MenuSystem instance = new MenuSystem();

    private SpriteBatch spriteBatch;
    private List<BaseMenuComponent> rootComponents;
    private Vector2i dragStartPosition;

    private BaseMenuComponent hoveredComponent;
    private BaseMenuComponent pressedComponent;
    private BaseMenuComponent draggedComponent;
    private Object draggedPayload;
    
    public MenuSystem() {
        this.rootComponents = new ArrayList<>();
        this.spriteBatch = new SpriteBatch();
        this.dragStartPosition = new Vector2i();
    }

    public void setup() {
        this.spriteBatch.setup();
    }

    public void addMenuComponent(BaseMenuComponent rootComponent) {
        rootComponent.computeInitialDimensions();
        rootComponent.computeDimensions(Config.instance.resolution);
        rootComponent.finalizeLayout(null, Vector2iMaths.zero);
        this.rootComponents.add(rootComponent);
    }

    public void update() {
        var mousePosition = Window.instance.getMousePosition();

        BaseMenuComponent hoveredComponent = null;
        for(var component : this.rootComponents) {
            var candidate = component.getHoveredComponent();
            if(candidate != null) {
                hoveredComponent = candidate;
                break;
            }
        }

        if(hoveredComponent != this.hoveredComponent) {
            if(this.hoveredComponent != null) {
                this.hoveredComponent.onEvent(new HoverEndEvent());
            }

            if(hoveredComponent != null) {
                hoveredComponent.onEvent(new HoverStartEvent());
            }

            this.hoveredComponent = hoveredComponent;
        }

        if(Window.instance.isButtonPressed(Button.mouseLeft)) {
            if(this.hoveredComponent != null) {
                this.hoveredComponent.onEvent(new PressStartEvent());
            }

            this.pressedComponent = this.hoveredComponent;
            this.dragStartPosition.set(mousePosition);

        }

        if(Window.instance.isButtonReleased(Button.mouseLeft)) {
            if(this.pressedComponent != null) {
                this.pressedComponent.onEvent(new PressEndEvent());
                if(this.pressedComponent == this.hoveredComponent) {
                    this.pressedComponent.onEvent(new ClickEvent());
                }
            }

            if(this.draggedComponent != null) {
                this.draggedComponent.onEvent(new DragEndEvent());

                if(this.hoveredComponent != null) {
                    var dropEvent = new DropEvent(this.draggedPayload);
                    this.hoveredComponent.onEvent(dropEvent);
                }
            }

            this.draggedComponent = null;
            this.pressedComponent = null;
        }

        if(this.pressedComponent != null) {
            var dragDistance = this.dragStartPosition.distance(mousePosition);
            if(this.pressedComponent.getDragDistanceThreshold() < dragDistance) {
                this.draggedComponent = this.pressedComponent;
                this.pressedComponent.onEvent(new PressEndEvent());
                var dragStartEvent = new DragStartEvent();
                this.draggedComponent.onEvent(dragStartEvent);
                this.draggedPayload = dragStartEvent.payload;
                this.pressedComponent = null;
            }
        }

        if(this.hoveredComponent != null) {
            this.hoveredComponent.onEvent(new HoverEvent());
        }

        if(this.pressedComponent != null) {
            this.pressedComponent.onEvent(new PressEvent());
        }

        if(this.draggedComponent != null) {
            this.draggedComponent.onEvent(new DragEvent());
        }
    }

    private Texture getZoneTexture() {
        var zone = Player.instance.getZone();
        return zone == null || zone.getZoneStatus() != ZoneStatus.ready 
            ? null 
            : zone.getTexture();
    }

    public void render() {
        this.spriteBatch.clear();
        for(var component : this.rootComponents) {
            component.writeToSpriteBatch(this.spriteBatch);
        }

        MenuCursor cursor;
        if(this.draggedComponent != null) {
            cursor = MenuCursor.dragCursor;
        } else if(this.hoveredComponent != null) {
            cursor = MenuCursor.pointerCursor;
        } else {
            cursor = MenuCursor.arrowCursor;
        }

        var gameState = Simulation.instance.getGameState();
        if(gameState != null && gameState.showCursor()) {
            cursor.writeToSpriteBatch(this.spriteBatch);
        }


        try(
            var blend = new BlendState();
            var zoneTextureState = new TextureState(SpriteShaderProgram.zoneTextureUnitID);
            var entityTextureState = new TextureState(SpriteShaderProgram.entityTextureUnitID);
            var overlayTextureState = new TextureState(SpriteShaderProgram.overlayTextureUnitID);
        ) {
            blend.setState(BlendMode.alphaBlend);
            zoneTextureState.setState(this.getZoneTexture());
            entityTextureState.setState(EntityTexture.instance);
            overlayTextureState.setState(OverlayTexture.instance);

            SpriteShaderProgram.instance.bind();

            int currentFrame = MeshTextureQuadStrategy.getAnimationFrame();
            SpriteShaderProgram.instance.setAnimationFrame(currentFrame);

            this.spriteBatch.render();
        }

    }
    
}
