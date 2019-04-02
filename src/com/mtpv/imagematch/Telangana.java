package com.mtpv.imagematch;

import java.io.Serializable;

public class Telangana implements Serializable {

    private String image;

    private String ps;

    private String present_address;

    private String link;

    private String conf;

    private String source;

    private String permenant_address;

    private String score;

    private String father_name;

    private String district;

    private String name;

    private String fir_no;

    private String id;

    private String age;

    private Case_info[] case_info;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getPresent_address() {
        return present_address;
    }

    public void setPresent_address(String present_address) {
        this.present_address = present_address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPermenant_address() {
        return permenant_address;
    }

    public void setPermenant_address(String permenant_address) {
        this.permenant_address = permenant_address;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFir_no() {
        return fir_no;
    }

    public void setFir_no(String fir_no) {
        this.fir_no = fir_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [image = "+image+", ps = "+ps+", present_address = "+present_address+", link = "+link+", conf = "+conf+", source = "+source+", permenant_address = "+permenant_address+", score = "+score+", father_name = "+father_name+", district = "+district+", name = "+name+", fir_no = "+fir_no+", id = "+id+", age = "+age+"]";
    }
}
