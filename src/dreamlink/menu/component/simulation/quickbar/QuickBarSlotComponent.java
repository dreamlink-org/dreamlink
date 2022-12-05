package dreamlink.menu.component.simulation.quickbar;

import org.joml.Vector2i;

import dreamlink.graphics.sprite.template.ISpriteTemplate;
import dreamlink.menu.component.core.BaseMenuComponent;
import dreamlink.menu.component.core.PaddingComponent;
import dreamlink.menu.component.core.WrapperComponent;
import dreamlink.menu.component.core.border.dialog.DialogBorderComponent;
import dreamlink.menu.component.core.border.dialog.DialogState;
import dreamlink.menu.component.core.box.BoxAlignment;
import dreamlink.menu.component.core.box.BoxComponent;
import dreamlink.menu.component.core.box.BoxDimension;
import dreamlink.menu.component.core.icon.IconComponent;
import dreamlink.menu.event.IEvent;
import dreamlink.menu.event.PressStartEvent;
import dreamlink.zone.IStamp;
import dreamlink.menu.event.DropEvent;

public class QuickBarSlotComponent extends WrapperComponent {

    private static Vector2i slotDimensions = new Vector2i(32);
    private static int padding = 2;

    private BaseMenuComponent component;
    private int slotID;
    private IQuickBarComponentDirectory directory;

    public QuickBarSlotComponent(IQuickBarComponentDirectory directory, int slotID) {
        this.directory = directory;
        this.slotID = slotID;
        this.component = new BoxComponent(
            new DialogBorderComponent(
                this::getDialogState,
                new PaddingComponent(
                    new IconComponent(
                        this::getSprite,
                        QuickBarSlotComponent.slotDimensions
                    ),
                    QuickBarSlotComponent.padding
                )
            ),
            BoxDimension.grow(),
            BoxDimension.grow(BoxAlignment.end)
        );
    }

    private DialogState getDialogState() {
        var selectedSlotID = this.directory.getSelectedSlotID();
        return this.slotID == selectedSlotID
            ? DialogState.focused
            : DialogState.blurred;
    }

    private ISpriteTemplate getSprite() {
        var stamp = this.directory.getStamp(this.slotID);
        return stamp != null ? stamp.getStampSprite() : null;
    }

    @Override
    public BaseMenuComponent getComponent() {
        return this.component;
    }

    @Override
    public void onEvent(IEvent event) {
        if(event instanceof PressStartEvent) {
            this.directory.setSelectedSlotID(this.slotID);
            return;
        }

        if(event instanceof DropEvent receiveEvent) {
            var droppedComponent = receiveEvent.droppedPayload;
            if(droppedComponent instanceof IStamp stamp) {
                this.directory.setStamp(this.slotID, stamp);
                return;
            }
        }
    }

    @Override
    public BaseMenuComponent getHoveredComponent() {
        return this.isMouseOver() ? this : null;
    }

}