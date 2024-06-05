package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes;

public class Booking {
    private String id;
    private String userId;
    private String hotelId;
    private String checkInDate; // Ngày checkin - sau 1 ngày của ngày tạo đơn
    private String checkOutDate; // Ngày checkout
    private int numberOfRooms; // Số lượng phòng thuê
    private int numAdult; // Số lượng người lớn có trong đoàn
    private int numKid; // Số lượng trẻ em có trong đoàn
    private int totalPrice;
    private String status; // "requestConfirm", "confirmed", "requestCancel", "cancelled", "requestComplete","completed"

    // Constructors
    public Booking() {
    }

    public Booking(String id, String userId, String hotelId, String checkInDate, String checkOutDate, int numberOfRooms, int numAdult, int numKid, int totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.hotelId = hotelId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfRooms = numberOfRooms;
        this.numAdult = numAdult;
        this.numKid = numKid;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getNumAdult() {
        return numAdult;
    }

    public void setNumAdult(int numAdult) {
        this.numAdult = numAdult;
    }

    public int getNumKid() {
        return numKid;
    }

    public void setNumKid(int numKid) {
        this.numKid = numKid;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
