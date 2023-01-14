package org.chefsantiago.service;

import lombok.Value;

@Value
public class Post {
    private String postTitle;
    private String postPermalink;
    private String postDatePretty;
    private String postImage;
    private String postExcerpt;
    private String postContent;
}
