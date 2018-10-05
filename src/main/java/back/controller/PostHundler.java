/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back.controller;

import back.model.Post;
import back.model.PostEvent;
import back.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 *
 * @author TOSHIBA
 */

@Component
public class PostHundler {

    private PostRepository repository;
    @Autowired
    private Twitter twitter;

    public PostHundler(PostRepository repository) {
        this.repository = repository;
    }

    public void addTagsToPost(Flux<Tweet> tweets) {
        tweets.flatMap(tweet -> {
            return ConvertTweetToPost(tweet);
        }).subscribe();
        System.out.println("Tweets ==== = == = = = =" + tweets.toString());
        repository.findAll()
                .subscribe(System.out::println);
    }

    public Mono<Post> ConvertTweetToPost(Tweet tweet) {
        Post p = new Post();
        p.setCreatedAt(tweet.getCreatedAt());
        p.setText(tweet.getText());
        p.setTags(tweet.getEntities().getHashTags());
        p.setUser(tweet.getUser());
        System.out.println(" p .get all hashtag " + p.getTags());
        repository.save(p).subscribe();
        return repository.save(p);
    }

    public Mono<ServerResponse> getAllPosts(ServerRequest request, String hashtag) {
        Flux<Post> posts = repository.findAll();
//        Flux<Tweet> tweets = Flux.fromIterable(twitter.searchOperations().search(hashtag).getTweets());
//        addTagsToPost(tweets);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(posts, Post.class);
    }

    public Mono<ServerResponse> getPost(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Post> postMono = this.repository.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return postMono
                .flatMap(post
                        -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(post)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> savePost(ServerRequest request) {
        Mono<Post> postMono = request.bodyToMono(Post.class);

        return postMono.flatMap(post
                -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(repository.save(post), Post.class));
    }

    public Mono<ServerResponse> updatePost(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Post> existingPostMono = this.repository.findById(id);
        Mono<Post> postMono = request.bodyToMono(Post.class);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return postMono.zipWith(existingPostMono,
                (post, existingPost)
                -> new Post(existingPost.getId(), post.getText(), post.getCreatedAt(), post.getTags(), post.getUser())
        )
                .flatMap(post
                        -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(repository.save(post), Post.class)
                ).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deletePost(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Post> postMono = this.repository.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return postMono
                .flatMap(existingPost
                        -> ServerResponse.ok()
                        .build(repository.delete(existingPost))
                )
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteAllPosts(ServerRequest request) {
        return ServerResponse.ok()
                .build(repository.deleteAll());
    }

    public Mono<ServerResponse> getPostEvents(ServerRequest request) {
        Flux<PostEvent> eventsFlux = Flux.interval(Duration.ofSeconds(1)).map(val
                -> new PostEvent(val, "Post Event")
        );

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(eventsFlux, PostEvent.class);
    }
}
