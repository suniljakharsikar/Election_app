package b2d.l.mahtmagandhi;

public class ChatData {
    String description;
    int likes;
    int dislikes;
    int image;

    public ChatData() {
    }

    public ChatData(String description, int likes, int dislikes, int image) {
        this.description = description;
        this.likes = likes;
        this.dislikes = dislikes;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
