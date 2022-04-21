package org.chefsantiago;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.chefsantiago.service.Post;
import org.chefsantiago.service.PostsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class GenerateSiteTest {

    @Autowired
    private PostsService postsService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void generateSite() throws Exception {
        Collection<Post> posts = postsService.getPosts();

        Assertions.assertNotNull(posts);
        Assertions.assertFalse(posts.isEmpty());

        File publicRoot = Path.of("public").toFile();
        File index = new File(publicRoot, "index.html");

        String homePage = testRestTemplate.getForObject("/", String.class);
        Files.writeString(index.toPath(), homePage);

        String page404 = testRestTemplate.getForObject("/404.html", String.class);
        Files.writeString(new File(publicRoot, "404.html").toPath(), page404);

        for (Post post : posts) {
            String page = testRestTemplate.getForObject(post.postPermalink(), String.class);
            File currentPageFile = new File(publicRoot, post.postPermalink() + "/index.html");
            FileUtils.createParentDirectories(currentPageFile);

            Files.writeString(currentPageFile.toPath(), page);
        }
    }

}
