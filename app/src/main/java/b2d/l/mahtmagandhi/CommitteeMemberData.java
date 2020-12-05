package b2d.l.mahtmagandhi;

public class CommitteeMemberData {
    int id;
    String first_name, last_name, designation, description, image;

    public CommitteeMemberData() {
    }

    public CommitteeMemberData(int id, String first_name, String last_name, String designation, String description, String image) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.designation = designation;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
