package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes;

public class Hotel {
    private String id;
    private String ownerId;
    private String name;
    private String provinceID;
    private String address;
    private String imageUrls;
    private int numRooms;
    private int numMaxGuest;
    private String amenities;
    private int price;

    // Constructors
    public Hotel() {
    }

    public Hotel(String id, String ownerId, String name,String address, String provinceID, String amenities,String imageUrls, int numRooms, int numMaxGuest, int price) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.provinceID = provinceID;
        this.address = address;
        this.imageUrls = imageUrls;
        this.numRooms = numRooms;
        this.numMaxGuest = numMaxGuest;
        this.amenities = amenities;
        this.price = price;
    }

    public Hotel(String ownerId, String name, String address, String provinceID, String amenities, String imageUrls, int price) {
        this.ownerId = ownerId;
        this.name = name;
        this.provinceID = provinceID;
        this.address = address;
        this.imageUrls = imageUrls;
        this.amenities = amenities;
        this.price = price;
    }

    // Getter and setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public int getNumMaxGuest() {
        return numMaxGuest;
    }

    public void setNumMaxGuest(int numMaxGuest) {
        this.numMaxGuest = numMaxGuest;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

}

