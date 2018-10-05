package back.controller;

import back.model.Post;
import back.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@RestController
@RequestMapping("/post")
public class PostController {
//    String consumerKey = "aCY9VL94qOjfCDrcUNmawIMPm"; // The application's consumer key
//    String consumerSecret = "xmC2vaq5tb5BFQHdPKb3W2NAx5f71zKbfd8CuELzvPuNdAG26s"; // The application's consumer secret
//    String accessToken = "2574519362-mvAPOMWBsr9NuwuqLy3Se8zYVCYjAufG24R46fe"; // The access token granted after OAuth authorization
//    String accessTokenSecret = "Qx1YWFMZATbXmCqxBlsKUnTvBRLJbPPVUKUoQInRe2GAv"; // The access token secret granted after OAuth authorization
//

    private PostRepository repository;
    @Autowired
    Twitter twitter;
//    Twitter twitter =  new TwitterTemplate(consumerKey,consumerSecret);
public Mono<ServerResponse> getAllPost(ServerRequest request) {
        Flux<Post> posts = repository.findAll(); 
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(posts, Post.class);
    }
//faire une recherche  sur l'api twitter
    @GetMapping(value = "/tweets/{hashtag}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Tweet> getAllTweets(@PathVariable final String hashtag, ServerRequest request) { 
        System.out.println(" testinnng"); 
        Flux<Tweet> tweets = request.bodyToFlux(Tweet.class);
        tweets = Flux.fromIterable(twitter.searchOperations().search(hashtag).getTweets()); 
        addTagsToPost(tweets);
        return tweets;

    }

    @DeleteMapping("/tweets/{id}")
    public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String tweetId) { 
        return repository.findById(tweetId)
                .flatMap(existingPost
                        -> repository.delete(existingPost)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public void addTagsToPost(Flux<Tweet> tweets ) {
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

    @PostConstruct
    public void init() {
//        List<Hashtag>hashtags = new ArrayList();
//        hashtags.add(new Hashtag(null,"a"));
//        hashtags.add(new Hashtag(null,"n"));
//        hashtags.add(new Hashtag(null,"b"));
//
//        Flux<Post> postFlux = Flux.just(
//                new Post(null, "Big Latte",hashtags),
//                new Post(null, "Big Decaf",hashtags),
//                new Post(null, "Green Tea",hashtags))
//                .flatMap(repository::save);
//        postFlux
//                .thenMany(repository.findAll())
//                .subscribe(System.out::println);

        System.out.println(repository.findAll());

    }

    public PostController(PostRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<Post> getAllPosts() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Post>> getPost(@PathVariable String id) {
        return repository.findById(id)
                .map(post -> ResponseEntity.ok(post))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    public Flux<Post> findbyname(@RequestParam(name = "x") String tag) {
//        Query query1 = new Query();
//        return  query1.addCriteria(Criteria.where("description").);
        System.out.println(" tag taga taga tagatagatagatagatagatgatagat");
        return repository.findPostByName(tag);
    }

    @GetMapping("/tag")
    public Flux<Post> findbytag(@RequestParam(name = "x") String tag) {
//        Query query1 = new Query();
//        return  query1.addCriteria(Criteria.where("description").);
        System.out.println(" ================================== ");
        return repository.findPostByTag(tag);
    }

    @DeleteMapping
    public Mono<Void> deleteAllPost() {
        return repository.deleteAll();
    }
}
