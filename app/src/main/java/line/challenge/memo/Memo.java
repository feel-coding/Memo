package line.challenge.memo;

import java.util.ArrayList;

public class Memo {
    private String title;
    private String memoContent;
    private ArrayList<String> imageUrl = new ArrayList<>();

    public Memo(String title, String memoContent) {
        this.title = title;
        this.memoContent = memoContent;
    }
    public void addUrl(String url) {
        imageUrl.add(url);
    }

    public String getTitle() {
        return title;
    }

    public String getMemoContent() {
        return memoContent;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    public void deleteUrl(String url) {
        imageUrl.remove(url);
    }
}
