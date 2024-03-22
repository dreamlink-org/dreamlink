package dreamlink.menu.component.simulation;

import org.joml.Vector2i;

import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.gamestate.simulation.SimulationLogViewerMenuGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.logger.Logger;
import dreamlink.menu.MenuSystem;
import dreamlink.menu.component.LogViewerComponent;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.dialog.DialogBorderComponent;
import dreamlink.menu.component.core.border.dialog.DialogState;
import dreamlink.menu.component.core.border.dialog.StaticDialogBorderComponentProvider;
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
import dreamlink.utility.maths.Vector4fMaths;

public class SimulationLogViewerComponent extends WrapperComponent {

    private static String windowTitle = "Log Viewer";
    private static Vector2i idealTextDimensions = new Vector2i(640, 480);
    private static int itemSpacing = 5;
    private static int logHistorySize = 1000;

    private static String backButtonText = "Back";
    private static Vector2i buttonDimensions = new Vector2i(160, 24);

    public static SimulationLogViewerComponent instance = new SimulationLogViewerComponent();

    private BaseMenuComponent component;
    private LogViewerComponent logViewerComponent;

    public SimulationLogViewerComponent() {
        this.logViewerComponent = new LogViewerComponent(SimulationLogViewerComponent.logHistorySize);
        this.component = new WindowComponent(
            OverlayTextureSample.iconFolder,
            SimulationLogViewerComponent.windowTitle,
            new PaddingComponent(
                new SpanComponent(SpanOrientation.vertical, SpanAlignment.center, SimulationLogViewerComponent.itemSpacing)
                    .addComponent(
                        new DialogBorderComponent(
                            new StaticDialogBorderComponentProvider(DialogState.focused),
                            new BoxComponent(
                                this.logViewerComponent,
                                BoxDimension.fixed(SimulationLogViewerComponent.idealTextDimensions.x),
                                BoxDimension.fixed(SimulationLogViewerComponent.idealTextDimensions.y)
                            )
                        )
                    ).addComponent(
                        new SpanComponent(SpanOrientation.horizontal, SpanAlignment.center, SimulationLogViewerComponent.itemSpacing)
                            .addComponent(
                                new ButtonComponent(
                                    new StaticButtonComponentProvider(this::back),
                                    new BoxComponent(
                                        new TextLineLabelComponent(
                                            SimulationLogViewerComponent.backButtonText,
                                            FontDecoration.normal,
                                            Vector4fMaths.black
                                        ),
                                        BoxDimension.fixed(SimulationLogViewerComponent.buttonDimensions.x),
                                        BoxDimension.fixed(SimulationLogViewerComponent.buttonDimensions.y)
                                    )
                                )
                            )
                    ),
                SimulationLogViewerComponent.itemSpacing
            )
        );
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    public void setup() {
        Logger.instance.debug("Setting up SimulationLogViewerComponent");
        MenuSystem.instance.addMenuComponent(this);
        this.logViewerComponent.setup();
    }

    private void back() {
        Simulation.instance.setGameState(SimulationGameState.instance);
    }

    private boolean isActive() {
        return Simulation.instance.getGameState() == SimulationLogViewerMenuGameState.instance;
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
