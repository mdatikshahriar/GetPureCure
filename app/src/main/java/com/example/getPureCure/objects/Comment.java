package com.example.getPureCure.objects;

public class Comment {
    String id, authorId, blogId, authorName, authorImageUri, content, date, replyCount;

    public Comment(String id, String authorId, String blogId, String authorName, String authorImageUri, String content, String date, String replyCount) {
        this.id = id;
        this.authorId = authorId;
        this.blogId = blogId;
        this.authorName = authorName;
        this.authorImageUri = authorImageUri;
        this.content = content;
        this.date = date;
        this.replyCount = replyCount;
    }

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getBlogId() {
        return blogId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorImageUri() {
        return authorImageUri;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getReplyCount() {
        return replyCount;
    }
}
