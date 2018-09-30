package back.controller;

import back.model.Hashtag;
import back.model.Post;
import back.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterServiceProvider;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.ws.Response;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Query;

@RestController
@RequestMapping("/post")
public class PostController {
    String consumerKey = "aCY9VL94qOjfCDrcUNmawIMPm"; // The application's consumer key
    String consumerSecret = "xmC2vaq5tb5BFQHdPKb3W2NAx5f71zKbfd8CuELzvPuNdAG26s"; // The application's consumer secret
    String accessToken = "2574519362-mvAPOMWBsr9NuwuqLy3Se8zYVCYjAufG24R46fe"; // The access token granted after OAuth authorization
    String accessTokenSecret = "Qx1YWFMZATbXmCqxBlsKUnTvBRLJbPPVUKUoQInRe2GAv"; // The access token secret granted after OAuth authorization


    private PostRepository repository;
    Twitter twitter =  new TwitterTemplate(consumerKey,consumerSecret);





//faire une recherche  sur l'api twitter
    @GetMapping(value = "/tweets/{hashtag}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  Flux<Tweet> getAllTweets (@PathVariable final String hashtag) {
       return Flux.fromIterable(twitter.searchOperations().search(hashtag).getTweets());

    }

    @PostConstruct
    public void init() {
        List<Hashtag>hashtags = new ArrayList();
        hashtags.add(new Hashtag(null,"a"));
        hashtags.add(new Hashtag(null,"n"));
        hashtags.add(new Hashtag(null,"b"));
    
        Flux<Post> postFlux = Flux.just(
                new Post(null, "Big Latte",hashtags),
                new Post(null, "Big Decaf",hashtags),
                new Post(null, "Green Tea",hashtags))
                .flatMap(repository::save);
        postFlux
                .thenMany(repository.findAll())
                .subscribe(System.out::println);

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
                .map(product -> ResponseEntity.ok(product))
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

//    public List<Poste> getPostesBytext(String poste) {
//
//        Query query = new Query();
//        query.addCriteria(Criteria.where("text").regex(poste));
//        List<Poste> postes = mongoTemplate.find(query,Poste.class);
//        return postes;
//    }
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Post> savePost(@RequestBody Post post) {
//        return repository.save(post);
//    }
//    @PutMapping("{id}")
//    public Mono<ResponseEntity<Post>> updatePost(@PathVariable(value = "id") String id,
//                                                       @RequestBody Post Post) {
//        return repository.findById(id)
//                .flatMap(existingProduct -> {
//                    existingProduct.setName(Post.getName());
//                    return repository.save(existingProduct);
//                })
//                .map(updatePost -> ResponseEntity.ok(updatePost))
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//    @DeleteMapping("{id}")
//    public Mono<ResponseEntity<Void>> deletePost(@PathVariable(value = "id") String id) {
//        return repository.findById(id)
//                .flatMap(existingProduct ->
//                        repository.delete(existingProduct)
//                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
//                )
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
    @DeleteMapping
    public Mono<Void> deleteAllPost() {
        return repository.deleteAll();
    }

//    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<PostE> getProductEvents() {
//        return Flux.interval(Duration.ofSeconds(1))
//                .map(val ->
//                        new PostEvent(val, "Product Event")
//                );
//    }
}
