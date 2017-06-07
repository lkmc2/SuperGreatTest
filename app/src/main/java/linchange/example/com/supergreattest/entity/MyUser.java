package linchange.example.com.supergreattest.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by Lin Change on 2017-02-17.
 */
//用户实体类
public class MyUser extends BmobUser {
    //父类默认包含username，password，email等属性

    private int age; //年龄
    private boolean sex; //性别:true为男，false为女
    private String summary; //简介

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
