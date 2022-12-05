package dreamlink.menu.event;

public class DropEvent implements IEvent {

    public Object droppedPayload;

    public DropEvent(Object droppedPayload) {
        this.droppedPayload = droppedPayload;
    }
    
}
