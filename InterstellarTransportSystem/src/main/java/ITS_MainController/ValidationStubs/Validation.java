package ITS_MainController.ValidationStubs;

public enum Validation {
    ROUTE_EXISTS(1, "ROUTE EXISTS"),
    ROUTE_TO_SELF(2, "ROUTE TO SELF"),
    TRAFFIC_EXISTS(3, "TRAFFIC EXISTS"),
    TRAFFIC_TO_SELF(4, "TRAFFIC TO SELF");

    //Enum variables
    final int id;
    final String label;

    Validation(final int id, final String label) {
        this.id = id;
        this.label = label;
    }

    public static Validation fromString(final String str) {
        for (Validation e : Validation.values()) {
            if (e.toString().equalsIgnoreCase(str)) {
                return e;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return label;
    }
}

