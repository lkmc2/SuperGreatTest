package linchange.example.com.supergreattest.entity;

/**
 * Created by Lin Change on 2017-02-22.
 */
//对话列表的实体类
public class ChatListData {

    private int type; //区分左对话框或右对话框的类型
    private String text; //对话内容

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ChatListData{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
