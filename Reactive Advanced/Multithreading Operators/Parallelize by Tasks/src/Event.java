import java.util.Objects;

public class Event {
    final String data;

    public Event(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return Objects.equals(data, event.data);
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}
