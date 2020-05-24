package org.marqueeview;

public class MarQueeData implements IMarqueeItem {
    private String title;

    private String url;

    public String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public CharSequence marqueeMessage() {
        if (content == null || content.length() == 0) {
            return title;
        }
        return title + "\n" + content;
    }

}
