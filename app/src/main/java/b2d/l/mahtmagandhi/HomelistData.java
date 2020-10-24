package b2d.l.mahtmagandhi;

public class HomelistData {
    String menuName;
    int iconId;

    public HomelistData() {
    }

    public HomelistData(String menuName, int iconId) {
        this.menuName = menuName;
        this.iconId = iconId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
