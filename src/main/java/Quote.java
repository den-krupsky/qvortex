import java.time.Instant;

public class Quote {
    private Instant time;
    private Float value;

    public Quote() {
    }

    public Quote(Instant time, Float value) {
        this.time = time;
        this.value = value;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return time.equals(quote.time);
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }
}
