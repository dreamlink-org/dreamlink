package dreamlink.menu.component.home;

import org.joml.Vector2i;

import dreamlink.gamestate.home.HomeExploreMenuGameState;
import dreamlink.gamestate.home.HomeLoadZoneGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.graphics.texture.sample.OverlayTextureSample;
import dreamlink.logger.Logger;
import dreamlink.menu.MenuSystem;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.dialog.DialogBorderComponent;
import dreamlink.menu.component.core.border.dialog.DialogState;
import dreamlink.menu.component.core.border.dialog.StaticDialogBorderComponentProvider;
import dreamlink.menu.component.core.box.BoxAlignment;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.button.ButtonComponent;
import dreamlink.menu.component.core.button.IButtonComponentProvider;
import dreamlink.menu.component.core.button.StaticButtonComponentProvider;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.core.text.TextCharacterState;
import dreamlink.menu.component.core.text.line.ITextLineComponentProvider;
import dreamlink.menu.component.core.text.line.TextLineComponent;
import dreamlink.menu.component.core.text.line.TextLineLabelComponent;
import dreamlink.menu.component.core.window.WindowComponent;
import dreamlink.player.Player;
import dreamlink.simulation.Simulation;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.ZoneCache;

public class HomeExploreComponent extends WrapperComponent {

    private class InternalExploreButtonComponentProvider implements IButtonComponentProvider {

        @Override
        public void onButtonClick() {
            HomeExploreComponent.this.explore();
        }

        @Override
        public boolean isButtonDisabled() {
            return HomeExploreComponent.this.zoneNameBuilder.length() == 0;
        }


    }

    private class InternalTextLineComponentProvider implements ITextLineComponentProvider {

        @Override
        public void setCharacterState(int characterIndex, TextCharacterState state) {
            state.set(
                HomeExploreComponent.this.zoneNameBuilder.charAt(characterIndex),
                FontDecoration.normal,
                Vector4fMaths.black
            );
        }

        @Override
        public int getCharacterCount() {
            return HomeExploreComponent.this.zoneNameBuilder.length();
        }

    }

    private static String windowTitle = "DreamLink: Main Menu";
    private static String exploreButtonText = "Explore";
    private static String quitButtonText = "Quit";
    private static String instructionsText = "Enter zone name:";
    private static int minInputWidth = 320;
    private static int textPadding = 2;
    private static Vector2i buttonDimensions = new Vector2i(160, 24);

    private static int itemSpacing = 5;

    public static HomeExploreComponent instance = new HomeExploreComponent();

    private BaseMenuComponent component;
    private StringBuilder zoneNameBuilder;

    public HomeExploreComponent() {
        this.zoneNameBuilder = new StringBuilder();
        this.component = new WindowComponent(
            OverlayTextureSample.iconFolder,
            HomeExploreComponent.windowTitle,
            new PaddingComponent(
                new SpanComponent(
                    SpanOrientation.vertical, 
                    SpanAlignment.center,
                    HomeExploreComponent.itemSpacing
                ).addComponent(
                    new BoxComponent(
                        new TextLineLabelComponent(
                            HomeExploreComponent.instructionsText,
                            FontDecoration.normal,
                            Vector4fMaths.black
                        ),
                        BoxDimension.grow(BoxAlignment.start),
                        BoxDimension.wrap()
                    )
                ).addComponent(
                    new DialogBorderComponent(
                        new StaticDialogBorderComponentProvider(DialogState.focused),
                        new BoxComponent(
                            new PaddingComponent(
                                new TextLineComponent(new InternalTextLineComponentProvider()),
                                HomeExploreComponent.textPadding
                            ),
                            BoxDimension.min(HomeExploreComponent.minInputWidth),
                            BoxDimension.wrap()
                        )
                    )
                ).addComponent(
                    new SpanComponent(SpanOrientation.horizontal, SpanAlignment.center, HomeExploreComponent.itemSpacing)
                        .addComponent(
                            new ButtonComponent(
                                new InternalExploreButtonComponentProvider(),
                                new BoxComponent(
                                    new TextLineLabelComponent(
                                        HomeExploreComponent.exploreButtonText,
                                        FontDecoration.normal,
                                        Vector4fMaths.black
                                    ),
                                    BoxDimension.fixed(HomeExploreComponent.buttonDimensions.x),
                                    BoxDimension.fixed(HomeExploreComponent.buttonDimensions.y)
                                )
                            )
                        ).addComponent(
                            new ButtonComponent(
                                new StaticButtonComponentProvider(Window.instance::setShouldClose),
                                new BoxComponent(
                                    new TextLineLabelComponent(
                                        HomeExploreComponent.quitButtonText,
                                        FontDecoration.normal,
                                        Vector4fMaths.black
                                    ),
                                    BoxDimension.fixed(HomeExploreComponent.buttonDimensions.x),
                                    BoxDimension.fixed(HomeExploreComponent.buttonDimensions.y)
                                )
                            )
                        )
                ),
                HomeExploreComponent.itemSpacing
            )
        );
    }

    public void clear() {
        this.zoneNameBuilder.setLength(0);
    }

    public void update() {
        if(Simulation.instance.getGameState() != HomeExploreMenuGameState.instance) {
            return;
        }

        for(var character : Window.instance.getCharacters()) {
            this.zoneNameBuilder.append(character);
        }

        if(Window.instance.isButtonPressed(Button.keyBackspace)) {
            if(this.zoneNameBuilder.length() > 0) {
                this.zoneNameBuilder.deleteCharAt(this.zoneNameBuilder.length() - 1);
            }
        }

        if(Window.instance.isButtonPressed(Button.keyEnter)) {
            this.explore();
        }
    }

    private void explore() {
        var zone = ZoneCache.instance.getZone(this.zoneNameBuilder.toString());
        Player.instance.setZone(zone);
        Simulation.instance.setGameState(HomeLoadZoneGameState.instance);
    }

    public void setup() {
        Logger.instance.debug("Setting up HomeExploreComponent");
        MenuSystem.instance.addMenuComponent(this);
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        return Simulation.instance.getGameState() == HomeExploreMenuGameState.instance
            ? super.getHoveredComponent() 
            : null;
    }

    @Override
    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        if(Simulation.instance.getGameState() == HomeExploreMenuGameState.instance) {
            super.writeToSpriteBatch(spriteBatch);
        }
    }
    
}
