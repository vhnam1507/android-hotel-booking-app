package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes;

public class Saved {
    String savedId, hotelId, userId;

    public Saved() {
    }

    public Saved(String savedId, String hotelId, String userId) {
        this.savedId = savedId;
        this.hotelId = hotelId;
        this.userId = userId;
    }

    public String getSavedId() {
        return savedId;
    }

    public void setSavedId(String savedId) {
        this.savedId = savedId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
