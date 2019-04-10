package org.nocode.timing.pojo;

/**
 * @Author HanZhao
 * @Description
 * @Date 2019/4/10
 */
public class User {

    /*此处和数据库字段保持一致*/
    private int id;
    private String name;
    private String school;
    private String profession;
    private String url;
    private String research;
    private String small_class;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", school='" + school + '\'' +
                ", profession='" + profession + '\'' +
                ", url='" + url + '\'' +
                ", research='" + research + '\'' +
                ", small_class='" + small_class + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public String getSmall_class() {
        return small_class;
    }

    public void setSmall_class(String small_class) {
        this.small_class = small_class;
    }

}
