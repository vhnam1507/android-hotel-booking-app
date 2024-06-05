package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes;

public class Province {
    private String id;
    private String name;

    // Default constructor required for calls to DataSnapshot.getValue(Province.class)
    public Province() {
    }

    public Province(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
