package org.chefsantiago.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final DateTimeFormatter PRETTY_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter PERMALINK_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final ImmutableMap<String, Post> POSTS_MAP;

    @Autowired
    public PostsService(ObjectMapper objectMapper) {
        try {
            MutableDataSet options = new MutableDataSet();
            options.set(HtmlRenderer.SOFT_BREAK, "<br/>\n");

            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();

            ResourceScanner resourceScanner = new ResourceScanner();
            String[] folderNames = resourceScanner.getResourcesNamesIn("/posts");

            ImmutableMap.Builder<String, Post> mapBuilder = ImmutableMap.builder();
            for (String slug : folderNames) {
                logger.info("Compiling post: {}", slug);

                InputStream metadataResource = resourceScanner.getResource("posts/" + slug + "/metadata.json").getInputStream();
                InputStream postResource = resourceScanner.getResource("posts/" + slug + "/post.md").getInputStream();

                Metadata metadata = objectMapper.readValue(metadataResource, Metadata.class);

                Document document = parser.parseReader(new BufferedReader(new InputStreamReader(postResource)));
                String postContent = renderer.render(document);

                String postPermalink = makePermalink(metadata.getPostDate(), slug);
                Post post = new Post(
                        metadata.getPostTitle(),
                        postPermalink,
                        PRETTY_FORMATTER.format(metadata.getPostDate()),
                        metadata.getBanner(),
                        metadata.getExcerpt(),
                        postContent);

                mapBuilder.put(postPermalink, post);
            }

            POSTS_MAP = mapBuilder.build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize posts", e);
        }
    }

    public Collection<Post> getPosts() {
        return POSTS_MAP.values();
    }

    public Post getPost(int year, int month, int day, String slug) {
        LocalDate postDate = LocalDate.of(year, month, day);
        String permalink = makePermalink(postDate, slug);
        return POSTS_MAP.get(permalink);
    }

    private String makePermalink(LocalDate postDate, String slug) {
        return String.format("/%s/%s/", PERMALINK_FORMATTER.format(postDate), slug);
    }

}
