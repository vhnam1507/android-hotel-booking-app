package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.customer;


import project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen.CurrentUserManager;

public class CurrentGroupDetail {
    private static CurrentGroupDetail instance;

    private  int numRoom = 1, numAdult = 1, numKid = 0;

    public CurrentGroupDetail() {
    }

    public static synchronized CurrentGroupDetail getInstance() {
        if (instance == null) {
            instance = new CurrentGroupDetail();
        }
        return instance;
    }

    public static void setInstance(CurrentGroupDetail instance) {
        CurrentGroupDetail.instance = instance;
    }

    public int getNumRoom() {
        return numRoom;
    }

    public void setNumRoom(int numRoom) {
        this.numRoom = numRoom;
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
}
