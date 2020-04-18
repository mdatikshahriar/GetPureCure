package com.example.getPureCure.objects;

import java.util.ArrayList;

public class Blog {
    private String id, authorId, title, titleImageUri, content, category, date, likeCount = "0", commentCount = "0";
    private ArrayList<String> tags;
    private ArrayList<BlogComments> blogComments;
    private ArrayList<BlogLikes> blogLikes;

    public Blog(String id, String authorId, String title, String titleImageUri, String content, String category, String date, String likeCount, String commentCount, ArrayList<String> tags) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.titleImageUri = titleImageUri;
        this.content = content;
        this.category = category;
        this.date = date;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.tags = tags;

        new ArrayList<BlogComments>();
        new ArrayList<BlogLikes>();
    }

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleImageUri() {
        return titleImageUri;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<BlogComments> getBlogComments() {
        return blogComments;
    }

    public ArrayList<BlogLikes> getBlogLikes() {
        return blogLikes;
    }

    public class BlogComments {
        private String blogId, userId, body, date;

        public BlogComments(String blogId, String userId, String body, String date) {
            this.blogId = blogId;
            this.userId = userId;
            this.body = body;
            this.date = date;
        }

        public String getBlogId() {
            return blogId;
        }

        public String getUserId() {
            return userId;
        }

        public String getBody() {
            return body;
        }

        public String getDate() {
            return date;
        }
    }

    public class BlogLikes {
        private String blogId, userId, date;

        public BlogLikes(String blogId, String userId, String date) {
            this.blogId = blogId;
            this.userId = userId;
            this.date = date;
        }

        public String getBlogId() {
            return blogId;
        }

        public String getUserId() {
            return userId;
        }

        public String getDate() {
            return date;
        }
    }
}
