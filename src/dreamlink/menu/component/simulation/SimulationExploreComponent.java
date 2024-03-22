package dreamlink.menu.component.simulation;

import org.joml.Vector2i;

import dreamlink.gamestate.home.HomeExploreMenuGameState;
import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.gamestate.simulation.SimulationMenuGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.logger.Logger;
import dreamlink.menu.MenuSystem;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.button.ButtonComponent;
import dreamlink.menu.component.core.button.StaticButtonComponentProvider;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.core.text.line.TextLineLabelComponent;
import dreamlink.menu.component.core.window.WindowComponent;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.Vector4fMaths;

public class SimulationExploreComponent extends WrapperComponent {

    private static String windowTitle = "Explore Menu";
    private static int itemSpacing = 5;

    private static String quitButtonText = "Quit";
    private static String backButtonText = "Back";
    private static Vector2i buttonDimensions = new Vector2i(160, 24);

    public static SimulationExploreComponent instance = new SimulationExploreComponent();

    private BaseMenuComponent component;

    public SimulationExploreComponent() {
        this.component = new WindowComponent(
            OverlayTextureSample.iconFolder,
            SimulationExploreComponent.windowTitle,
            new PaddingComponent(
                new SpanComponent(SpanOrientation.vertical, SpanAlignment.center, SimulationExploreComponent.itemSpacing)
                    .addComponent(
                        new ButtonComponent(
                            new StaticButtonComponentProvider(this::back),
                            new BoxComponent(
                                new TextLineLabelComponent(
                                    SimulationExploreComponent.backButtonText,
                                    FontDecoration.normal,
                                    Vector4fMaths.black
                                ),
                                BoxDimension.fixed(SimulationExploreComponent.buttonDimensions.x),
                                BoxDimension.fixed(SimulationExploreComponent.buttonDimensions.y)
                            )
                        )
                    )
                    .addComponent(
                        new ButtonComponent(
                            new StaticButtonComponentProvider(this::quit),
                            new BoxComponent(
                                new TextLineLabelComponent(
                                    SimulationExploreComponent.quitButtonText,
                                    FontDecoration.normal,
                                    Vector4fMaths.black
                                ),
                                BoxDimension.fixed(SimulationExploreComponent.buttonDimensions.x),
                                BoxDimension.fixed(SimulationExploreComponent.buttonDimensions.y)
                            )
                        )
                    ),
                SimulationExploreComponent.itemSpacing
            )
        );
    }

    private void quit() {
        Simulation.instance.setGameState(HomeExploreMenuGameState.instance);
    }

    private void back() {
        Simulation.instance.setGameState(SimulationGameState.instance);
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    public void setup() {
        Logger.instance.debug("Setting up SimulationExploreComponent");
        MenuSystem.instance.addMenuComponent(this);
    }

    private boolean isActive() {
        return true
            && Simulation.instance.getGameState() == SimulationMenuGameState.instance
            && Simulation.instance.simulationMode == SimulationMode.explore;

    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        return this.isActive()
            ? super.getHoveredComponent() 
            : null;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        if(this.isActive()) {
            super.writeToSpriteBatch(spriteBatch);
        }
    }

    
}
