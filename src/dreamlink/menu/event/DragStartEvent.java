package dreamlink.menu.event;

public class DragStartEvent implements IEvent {

    public Object payload;

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}
