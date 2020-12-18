package b2d.l.mahtmagandhi;

public class Appointment_data {
    String date, status, detail;

    public Appointment_data() {
    }

    public Appointment_data(String date, String status, String detail) {
        this.date = date;
        this.status = status;
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
