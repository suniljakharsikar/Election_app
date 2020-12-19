package b2d.l.mahtmagandhi;

public class CommentData {
    int id, parent_id, user_id;
    String comment, created_at, username, userImage;

    public CommentData() {
    }

    public CommentData(int id, int parent_id, int user_id, String comment, String created_at, String username, String userImage) {
        this.id = id;
        this.parent_id = parent_id;
        this.user_id = user_id;
        this.comment = comment;
        this.created_at = created_at;
        this.username = username;
        this.userImage = userImage;
    }

    public CommentData(String username, String comment) {

        this.username = username;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
