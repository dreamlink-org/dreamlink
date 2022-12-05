package dreamlink.menu.component.simulation;

import org.joml.Vector2i;

import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.gamestate.simulation.SimulationMessageViewerMenuGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.menu.MenuSystem;
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
import dreamlink.menu.component.core.text.area.TextAreaComponent;
import dreamlink.menu.component.core.text.area.WrappedTextAreaComponentProvider;
import dreamlink.menu.component.core.text.line.TextLineLabelComponent;
import dreamlink.menu.component.core.window.WindowComponent;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.Vector4fMaths;

public class SimulationMessageViewerComponent extends WrapperComponent {

    private static int itemSpacing = 5;
    private static String windowTitle = "Message Viewer";
    private static String backText = "Back";
    private static Vector2i idealTextDimensions = new Vector2i(360, 240);
    private static Vector2i buttonDimensions = new Vector2i(160, 24);

    public static SimulationMessageViewerComponent instance = new SimulationMessageViewerComponent();

    private WrappedTextAreaComponentProvider messageProvider;
    private BaseMenuComponent component;
    private TextAreaComponent textAreaComponent;
    
    private SimulationMessageViewerComponent() {
        this.messageProvider = new WrappedTextAreaComponentProvider();
        this.textAreaComponent = new TextAreaComponent(this.messageProvider);
        this.component = new WindowComponent(
            OverlayTextureSample.iconFolder,
            SimulationMessageViewerComponent.windowTitle,
            new PaddingComponent(
                new SpanComponent(
                    SpanOrientation.vertical,
                    SpanAlignment.center,
                    SimulationMessageViewerComponent.itemSpacing
                )
                    .addComponent(
                        new DialogBorderComponent(
                            new StaticDialogBorderComponentProvider(DialogState.focused),
                            new BoxComponent(
                                this.textAreaComponent,
                                BoxDimension.fixed(SimulationMessageViewerComponent.idealTextDimensions.x),
                                BoxDimension.fixed(SimulationMessageViewerComponent.idealTextDimensions.y)
                            )
                        )
                    )
                    .addComponent(
                        new ButtonComponent(
                            new StaticButtonComponentProvider(this::back),
                            new BoxComponent(
                                new TextLineLabelComponent(
                                    SimulationMessageViewerComponent.backText,
                                    FontDecoration.normal,
                                    Vector4fMaths.black
                                ),
                                BoxDimension.fixed(SimulationMessageViewerComponent.buttonDimensions.x),
                                BoxDimension.fixed(SimulationMessageViewerComponent.buttonDimensions.y)
                            )
                        )
                    ),
                SimulationMessageViewerComponent.itemSpacing
            )
        );
    }

    private void back() {
        Simulation.instance.setGameState(SimulationGameState.instance);
    }

    public void setMessage(String text) {
        this.messageProvider.set(
            text,
            FontDecoration.normal,
            Vector4fMaths.black,
            this.textAreaComponent.getVisibleCharacterCount()
        );
    }

    public void setup() {
        MenuSystem.instance.addMenuComponent(this);
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        return Simulation.instance.getGameState() == SimulationMessageViewerMenuGameState.instance
            ? super.getHoveredComponent() 
            : null;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        if(Simulation.instance.getGameState() == SimulationMessageViewerMenuGameState.instance) {
            super.writeToSpriteBatch(spriteBatch);
        }
    }
}
