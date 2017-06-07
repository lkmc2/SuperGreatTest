package linchange.example.com.supergreattest.entity;

/**
 * Created by Lin Change on 2017-02-21.
 */
//快递查询实体类
public class CourierData {
    private String datetime; //快递时间
    private String remark; //快递状态
    private String zone; //已到达城市

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "datetime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
