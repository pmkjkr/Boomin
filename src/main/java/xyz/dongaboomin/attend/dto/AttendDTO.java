package xyz.dongaboomin.attend.dto;

/**
 * Created by horse on 2017. 8. 16..
 */
public class AttendDTO {
    private int id;
    private int stuId;
    private String name;
    private String major;
    private int check_att;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getCheck_att() {
        return check_att;
    }

    public void setCheck_att(int check_att) {
        this.check_att = check_att;
    }
}
