package back.model;

/**
 * Created by TOSHIBA on 25/09/2018.
 */
public class PostEvent {
    private Long eventId;

    private String eventType;

    public PostEvent(Long eventId, String eventType) {
        this.eventId = eventId;
        this.eventType = eventType;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "PostEvent{" +
                "eventId=" + eventId +
                ", eventType='" + eventType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostEvent postEvent = (PostEvent) o;

        if (eventId != null ? !eventId.equals(postEvent.eventId) : postEvent.eventId != null) return false;
        return eventType != null ? eventType.equals(postEvent.eventType) : postEvent.eventType == null;

    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        return result;
    }
}
