package dreamlink.menu.component.simulation.quickbar;

import java.util.ArrayList;
import java.util.List;

import dreamlink.gamestate.IGameState;
import dreamlink.gamestate.simulation.SimulationGameState;
import dreamlink.gamestate.simulation.SimulationMenuGameState;
import dreamlink.graphics.sprite.SpriteBatch;
import dreamlink.graphics.text.FontDecoration;
import dreamlink.logger.Logger;
import dreamlink.menu.MenuSystem;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.WindowBorderComponent;
import dreamlink.menu.component.core.box.BoxAlignment;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.span.SpanAlignment;
import dreamlink.menu.component.core.span.SpanComponent;
import dreamlink.menu.component.core.span.SpanOrientation;
import dreamlink.menu.component.core.text.TextCharacterState;
import dreamlink.menu.component.core.text.line.ITextLineComponentProvider;
import dreamlink.menu.component.core.text.line.TextLineComponent;
import dreamlink.simulation.Simulation;
import dreamlink.simulation.SimulationMode;
import dreamlink.utility.maths.Vector4fMaths;
import dreamlink.window.Window;
import dreamlink.window.button.Button;
import dreamlink.zone.IStamp;

public class QuickBarComponent extends WrapperComponent {

    private class QuickBarComponentDirectory implements IQuickBarComponentDirectory {

        @Override
        public int getSelectedSlotID() {
            return QuickBarComponent.instance.getSelectedSlotID();
        }

        @Override
        public void setSelectedSlotID(int slotID) {
            QuickBarComponent.instance.setSelectedSlotID(slotID);
        }

        @Override
        public IStamp getStamp(int slotID) {
            return QuickBarComponent.instance.getStamp(slotID);
        }

        @Override
        public void setStamp(int slotID, IStamp stamp) {
            QuickBarComponent.instance.setStamp(slotID, stamp);
        }

    }

    private class InternalTextLineComponentProvider implements ITextLineComponentProvider {

        @Override
        public int getCharacterCount() {
            var selectedStamp = QuickBarComponent.instance.getSelectedStamp();
            if(selectedStamp == null) {
                return 0;
            }
            
            return 0
                + selectedStamp.getStampName().length()
                + selectedStamp.getStampType().length()
                + 2;
        }

        @Override
        public void setCharacterState(int characterIndex, TextCharacterState state) {
            var selectedStamp = QuickBarComponent.instance.getSelectedStamp();
            if(selectedStamp == null) {
                state.clear();
                return;
            }
            
            var stampType = String.format("%s: ", selectedStamp.getStampType());
            var stampName = selectedStamp.getStampName();

            if(characterIndex < stampType.length()) {
                state.set(
                    stampType.charAt(characterIndex),
                    FontDecoration.normal,
                    Vector4fMaths.black
                );
                return;
            }

            characterIndex -= stampType.length();
            if(characterIndex < stampName.length()) {
                state.set(
                    stampName.charAt(characterIndex),
                    FontDecoration.underline,
                    Vector4fMaths.blue
                );
                return;
            }

            state.clear();
        }
    }

    private static IGameState[] activeGameStates = new IGameState[] {
        SimulationGameState.instance,
        SimulationMenuGameState.instance
    };

    private static int numQuickBarSlots = 10;
    private static int spacing = 5;
    private static int numStampSlots = 10;

    public static QuickBarComponent instance = new QuickBarComponent();

    private static Button[] quickBarKeys = new Button[] {
        Button.key1,
        Button.key2,
        Button.key3,
        Button.key4,
        Button.key5,
        Button.key6,
        Button.key7,
        Button.key8,
        Button.key9,
        Button.key0,
    };

    private IStamp[] slotData;
    private List<QuickBarSlotComponent> slots;
    private BaseMenuComponent component;

    private int selectedSlotID;

    private QuickBarComponent() {
        this.slots = new ArrayList<>();
        this.slotData = new IStamp[QuickBarComponent.numStampSlots];

        var layoutComponent = new SpanComponent(
            SpanOrientation.horizontal, 
            SpanAlignment.start, 
            QuickBarComponent.spacing
        );

        var directory = new QuickBarComponentDirectory();
        for(var ix = 0; ix < QuickBarComponent.numQuickBarSlots; ix += 1) {
            var slot = new QuickBarSlotComponent(directory, ix);
            this.slots.add(slot);
            layoutComponent.addComponent(slot);
        }

        this.component = new BoxComponent(
            new WindowBorderComponent(
                new BoxComponent(
                    new PaddingComponent(
                        new SpanComponent(SpanOrientation.vertical, SpanAlignment.start, QuickBarComponent.spacing)
                            .addComponent(
                                new TextLineComponent(new InternalTextLineComponentProvider())
                            )
                            .addComponent(layoutComponent),
                        spacing
                    ),
                    BoxDimension.wrap(),
                    BoxDimension.wrap()
                )
            ),
            BoxDimension.grow(),
            BoxDimension.grow(BoxAlignment.end)
        );
    }

    private void setSelectedSlotID(int slotID) {
        this.selectedSlotID = slotID;
    }

    private int getSelectedSlotID() {
        return this.selectedSlotID;
    }

    private IStamp getStamp(int slotID) {
        return this.slotData[slotID];
    }

    private void setStamp(int slotID, IStamp stamp) {
        this.slotData[slotID] = stamp;
    }

    public IStamp getSelectedStamp() {
        if(this.selectedSlotID < 0 || this.selectedSlotID >= this.slotData.length) {
            return null;
        }
        return this.slotData[this.selectedSlotID];
    }

    public void setup() {
        Logger.instance.debug("Setting up QuickBarComponent");
        MenuSystem.instance.addMenuComponent(this);
    }

    private boolean isGameStateActive() {
        var gameState = Simulation.instance.getGameState();
        for(var state : QuickBarComponent.activeGameStates) {
            if(state == gameState) {
                return true;
            }
        }
        return false;
    }

    private boolean isActive() {
        return true
            && this.isGameStateActive()
            && Simulation.instance.simulationMode == SimulationMode.edit;
    }

    public void update() {
        if(!this.isActive()) {
            return;
        }

        for(var ix = 0; ix < QuickBarComponent.quickBarKeys.length; ix += 1) {
            if(Window.instance.isButtonPressed(QuickBarComponent.quickBarKeys[ix])) {
                this.setSelectedSlotID(ix);
            }
        }
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    public void writeToSpriteBatch(SpriteBatch spriteBatch) {
        if(this.isActive()) {
            super.writeToSpriteBatch(spriteBatch);
        }
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        return this.isActive() ? super.getHoveredComponent() : null;
    }
}
