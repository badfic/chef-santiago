package org.chefsantiago.web;

import org.chefsantiago.service.Post;
import org.chefsantiago.service.PostsService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {

    private final PostsService postsService;

    @Autowired
    public HomeController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping(value = {"/", "/index.html"}, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getHome() {
        Map<String, Object> props = commonProps();
        props.put("pageTitle", "Chef Santiago");
        props.put("posts", postsService.getPosts());
        return new ModelAndView("home", props);
    }

    @GetMapping(value = "/{year}/{month}/{day}/{slug}/", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndividualPost(@PathVariable int year, @PathVariable int month, @PathVariable int day, @PathVariable String slug) {
        Post post = postsService.getPost(year, month, day, slug);

        if (post == null) {
            return new ModelAndView("404", commonProps());
        }

        Map<String, Object> props = commonProps();
        props.put("pageTitle", post.postTitle());
        props.put("post", post);
        return new ModelAndView("post", props);
    }

}
