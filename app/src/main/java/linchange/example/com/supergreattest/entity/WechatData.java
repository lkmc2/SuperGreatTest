package linchange.example.com.supergreattest.entity;

/**
 * Created by Lin Change on 2017-02-23.
 */
//微信文章实体类
public class WechatData {

    private String title; //文章标题
    private String source; //文章出处
    private String imgUrl; //图片的url
    private String newsUrl; //新闻地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    @Override
    public String toString() {
        return "WechatData{" +
                "title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", newsUrl='" + newsUrl + '\'' +
                '}';
    }
}
