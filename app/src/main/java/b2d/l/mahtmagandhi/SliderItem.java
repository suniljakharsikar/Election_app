package b2d.l.mahtmagandhi;

public class SliderItem {
    public String getDescription() {
        return "testing";
    }

    public String getImageUrl(int position) {
        switch (position) {
            case 0:
                return "https://tableforchange.com/wp-content/uploads/2019/12/Mahatma-Gandhi-i-a1.jpg";
            case 1:
                return "https://www.pulitzer.org/cms/sites/default/files/styles/page_photo/public/main_images/mahatma-gandhi960.jpg";
            case 2:
                return "https://images.newindianexpress.com/uploads/user/imagelibrary/2019/9/29/w900X450/Gandhi_Wiki.jpg";
            default:
                return "https://images.newindianexpress.com/uploads/user/imagelibrary/2019/9/29/w900X450/Gandhi_Wiki.jpg";

        }
    }
}
