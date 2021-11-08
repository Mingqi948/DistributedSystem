package service.message;

public class RequestDeadline {

    private int SEED_ID;

    public RequestDeadline(int id) {
        SEED_ID = id;
    }

    public void setSEED_ID(int SEED_ID) {
        this.SEED_ID = SEED_ID;
    }

    public int getSEED_ID() {
        return SEED_ID;
    }

}
