package pgu.sample.client;

public class Person {
    private String name;
    private double start;
    private double end;

    public Person(final String name, final double start, final double end) {
        super();
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getStart() {
        return start;
    }

    public void setStart(final double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(final double end) {
        this.end = end;
    }

}
