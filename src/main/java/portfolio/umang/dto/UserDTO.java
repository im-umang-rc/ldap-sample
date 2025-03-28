package portfolio.umang.dto;

public class UserDTO {
    private String uid;
    private boolean locked;

    public UserDTO() {
    }

    public UserDTO(String uid, boolean locked) {
        this.uid = uid;
        this.locked = locked;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
