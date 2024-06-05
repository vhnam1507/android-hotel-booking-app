package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes;

public class Review {
    private String reviewId, hotelId, avatarUrl, userMail, bookingId, detail, date;
    private int rate;

    public Review() {
    }

    public Review(String reviewId, String hotelId, String avatarUrl, String userMail, String bookingId, String detail, String date, int rate) {
        this.reviewId = reviewId;
        this.hotelId = hotelId;
        this.avatarUrl = avatarUrl;
        this.userMail = userMail;
        this.bookingId = bookingId;
        this.detail = detail;
        this.date = date;
        this.rate = rate;
    }

    public Review(String hotelId, String avatarUrl, String userMail, String bookingId, String detail, String date, int rate) {
        this.hotelId = hotelId;
        this.avatarUrl = avatarUrl;
        this.userMail = userMail;
        this.bookingId = bookingId;
        this.detail = detail;
        this.date = date;
        this.rate = rate;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
