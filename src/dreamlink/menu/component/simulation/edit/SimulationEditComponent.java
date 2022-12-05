package dreamlink.menu.component.simulation.edit;

import org.joml.Vector2i;

import dreamlink.gamestate.simulation.SimulationMenuGameState;
import dreamlink.gamestate.simulation.SimulationZoneSaveBusyGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
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
import dreamlink.menu.component.core.tab.TabViewComponent;
import dreamlink.menu.component.core.text.line.TextLineLabelComponent;
import dreamlink.menu.component.core.window.WindowComponent;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.window.Window;

public class SimulationEditComponent extends WrapperComponent {

    private static int itemSpacing = 5;
    private static String saveText = "Save";
    private static String quitText = "Quit";
    private static String windowTitle = "Edit Menu";
    private static String blocksTabLabel = "Blocks";
    private static String doorsTabLabel = "Doors";
    private static String speakersTabLabel = "Speakers";
    private static Vector2i idealDimensions = new Vector2i(360, 360);
    private static Vector2i buttonDimensions = new Vector2i(160, 24);

    public static SimulationEditComponent instance = new SimulationEditComponent();
    
    private BaseMenuComponent component;

    private SimulationEditComponent() {
        this.component = new WindowComponent(
            OverlayTextureSample.iconFolder, 
            SimulationEditComponent.windowTitle,
            new PaddingComponent(
                new BoxComponent(
                    new SpanComponent(
                        SpanOrientation.vertical, 
                        SpanAlignment.center, 
                        SimulationEditComponent.itemSpacing
                    )
                        .addComponent(
                            new TabViewComponent()
                                .addTab(
                                    new TextLineLabelComponent(
                                        SimulationEditComponent.blocksTabLabel,
                                        FontDecoration.normal,
                                        Vector4fMaths.black
                                    ),
                                    new BlockListComponent()
                                )
                                .addTab(
                                    new TextLineLabelComponent(
                                        SimulationEditComponent.doorsTabLabel,
                                        FontDecoration.normal,
                                        Vector4fMaths.black
                                    ),
                                    new DoorListComponent()
                                )
                                .addTab(
                                    new TextLineLabelComponent(
                                        SimulationEditComponent.speakersTabLabel,
                                        FontDecoration.normal,
                                        Vector4fMaths.black
                                    ),
                                    new SpeakerListComponent()
                                )
                        ).addComponent(
                            new SpanComponent(
                                SpanOrientation.horizontal,
                                SpanAlignment.center,
                                SimulationEditComponent.itemSpacing
                            )
                                .addComponent(
                                    new ButtonComponent(
                                        new StaticButtonComponentProvider(this::save),
                                        new BoxComponent(
                                            new TextLineLabelComponent(
                                                SimulationEditComponent.saveText,
                                                FontDecoration.normal,
                                                Vector4fMaths.black
                                            ),
                                            BoxDimension.fixed(SimulationEditComponent.buttonDimensions.x),
                                            BoxDimension.fixed(SimulationEditComponent.buttonDimensions.y)
                                        )
                                    )
                                )
                                .addComponent(
                                    new ButtonComponent(
                                        new StaticButtonComponentProvider(Window.instance::setShouldClose),
                                        new BoxComponent(
                                            new TextLineLabelComponent(
                                                SimulationEditComponent.quitText,
                                                FontDecoration.normal,
                                                Vector4fMaths.black
                                            ),
                                            BoxDimension.fixed(SimulationEditComponent.buttonDimensions.x),
                                            BoxDimension.fixed(SimulationEditComponent.buttonDimensions.y)
                                        )
                                    )
                                )
                        ),
                    BoxDimension.min(SimulationEditComponent.idealDimensions.x),
                    BoxDimension.min(SimulationEditComponent.idealDimensions.y)
                ),
                SimulationEditComponent.itemSpacing
            )
        );
    }

    private void save() {
        Simulation.instance.setGameState(SimulationZoneSaveBusyGameState.instance);
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
        var isActive = true
            && Simulation.instance.getGameState() == SimulationMenuGameState.instance
            && Simulation.instance.simulationMode == SimulationMode.edit;
        return isActive ? super.getHoveredComponent() : null;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        if(
            true
            && Simulation.instance.getGameState() == SimulationMenuGameState.instance
            && Simulation.instance.simulationMode == SimulationMode.edit
        ) {
            super.writeToSpriteBatch(spriteBatch);
        }
    }

}
