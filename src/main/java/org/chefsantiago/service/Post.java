package org.chefsantiago.service;

import java.time.LocalDate;
import lombok.Value;

@Value
public class Post {
    String postTitle;
    String postPermalink;
    LocalDate postDate;
    String postDatePretty;
    String postImage;
    String postExcerpt;
    String postContent;
}
