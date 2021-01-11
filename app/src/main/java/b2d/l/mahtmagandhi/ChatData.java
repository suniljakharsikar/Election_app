package b2d.l.mahtmagandhi;

public class ChatData {
    String id, user_id, title, description, image_name;
    int likes, dislike, likeStatus, UnlikeStatus, commentCount;

    public ChatData() {
    }

    public ChatData(String id, String user_id, String title, String description, String image_name, int likes, int dislike, int likeStatus, int unlikeStatus, int commentCount) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.image_name = image_name;
        this.likes = likes;
        this.dislike = dislike;
        this.likeStatus = likeStatus;
        UnlikeStatus = unlikeStatus;
        this.commentCount = commentCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public int getUnlikeStatus() {
        return UnlikeStatus;
    }

    public void setUnlikeStatus(int unlikeStatus) {
        UnlikeStatus = unlikeStatus;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
