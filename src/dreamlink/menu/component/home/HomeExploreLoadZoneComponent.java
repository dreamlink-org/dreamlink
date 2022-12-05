package dreamlink.menu.component.home;

import org.joml.Vector2i;

import dreamlink.gamestate.home.HomeExploreMenuGameState;
import dreamlink.gamestate.home.HomeLoadZoneGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
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
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.window.Window;

public class HomeExploreLoadZoneComponent extends WrapperComponent {

    private static String windowTitle = "Explore Level Loader";
    private static Vector2i idealTextDimensions = new Vector2i(640, 480);
    private static int itemSpacing = 5;
    private static int logHistorySize = 1000;

    private static String quitButtonText = "Quit";
    private static String backButtonText = "Back";
    private static Vector2i buttonDimensions = new Vector2i(160, 24);

    public static HomeExploreLoadZoneComponent instance = new HomeExploreLoadZoneComponent();

    private BaseMenuComponent component;
    private LogViewerComponent logViewerComponent;

    public HomeExploreLoadZoneComponent() {
        this.logViewerComponent = new LogViewerComponent(HomeExploreLoadZoneComponent.logHistorySize);
        this.component = new WindowComponent(
            OverlayTextureSample.iconFolder,
            HomeExploreLoadZoneComponent.windowTitle,
            new PaddingComponent(
                new SpanComponent(SpanOrientation.vertical, SpanAlignment.center, HomeExploreLoadZoneComponent.itemSpacing)
                    .addComponent(
                        new DialogBorderComponent(
                            new StaticDialogBorderComponentProvider(DialogState.focused),
                            new BoxComponent(
                                this.logViewerComponent,
                                BoxDimension.fixed(HomeExploreLoadZoneComponent.idealTextDimensions.x),
                                BoxDimension.fixed(HomeExploreLoadZoneComponent.idealTextDimensions.y)
                            )
                        )
                    )
                    .addComponent(
                        new SpanComponent(SpanOrientation.horizontal, SpanAlignment.center, HomeExploreLoadZoneComponent.itemSpacing)
                            .addComponent(
                                new ButtonComponent(
                                    new StaticButtonComponentProvider(this::back),
                                    new BoxComponent(
                                        new TextLineLabelComponent(
                                            HomeExploreLoadZoneComponent.backButtonText,
                                            FontDecoration.normal,
                                            Vector4fMaths.black
                                        ),
                                        BoxDimension.fixed(HomeExploreLoadZoneComponent.buttonDimensions.x),
                                        BoxDimension.fixed(HomeExploreLoadZoneComponent.buttonDimensions.y)
                                    )
                                )
                            )
                            .addComponent(
                                new ButtonComponent(
                                    new StaticButtonComponentProvider(Window.instance::setShouldClose),
                                    new BoxComponent(
                                        new TextLineLabelComponent(
                                            HomeExploreLoadZoneComponent.quitButtonText,
                                            FontDecoration.normal,
                                            Vector4fMaths.black
                                        ),
                                        BoxDimension.fixed(HomeExploreLoadZoneComponent.buttonDimensions.x),
                                        BoxDimension.fixed(HomeExploreLoadZoneComponent.buttonDimensions.y)
                                    )
                                )
                            )
                    ),
                HomeExploreLoadZoneComponent.itemSpacing
            )
        );
    }

    private void back() {
        Simulation.instance.setGameState(HomeExploreMenuGameState.instance);
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    public void setup() {
        MenuSystem.instance.addMenuComponent(this);
        this.logViewerComponent.setup();
    }

    private boolean isActive() {
        return true
            && Simulation.instance.getGameState() == HomeLoadZoneGameState.instance
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
