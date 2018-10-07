package back.controller;

import back.model.Post;
import back.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
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
// premier méthode tester le save  je le fait pour  chaque objet 

    @GetMapping(value = "/firsttweets/{hashtag}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Flux<Tweet> getAllTweets(@PathVariable final String hashtag) {
        Flux<Tweet> tweets = Flux.fromIterable(twitter.searchOperations().search("#" + hashtag).getTweets());
        //La fonction qui enrégistre les tweet dans la base des données 
        addTagsToPost(tweets);
        return tweets;

    }
     public void addTagsToPost(Flux<Tweet> tweets) {
        tweets.flatMap(tweet -> {
//            Convertire le résultat en tweets  et enrégistré chacun d'eux dans la base des données
            return ConvertTweetToPost(tweet);
        });
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
        repository.save(p).subscribe();
        return Mono.just(p);
    }
    
    
  ///////////////////////////////////////////////////////////////////////////////
    
// 2émme  méthode le save pour tous les tweet en méme temps convertir et enrégistrer les tweet tous en méme temps
    // bémole  j'ai utilisé ds list Stream    
    @GetMapping(value = "/tweets/{hashtag}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Flux<Post> getAllPostes(@PathVariable final String hashtag) {
        Flux<Post> postes = ConvertAllTweetToPost(twitter.searchOperations().search("#" + hashtag).getTweets());
        //La fonction qui enrégistre les tweet dans la base des données  
        return postes;

    }
    public Flux<Post> ConvertAllTweetToPost(List<Tweet> tweets) {
        List<Post> postes = new ArrayList();
        tweets.stream()
                .forEach(tw -> {
                    Post p = new Post();
                    p.setCreatedAt(tw.getCreatedAt());
                    p.setText(tw.getText());
                    p.setTags(tw.getEntities().getHashTags());
                    p.setUser(tw.getUser());
                    postes.add(p);
                });
        repository.saveAll(postes)
                .subscribe();
        return Flux.fromIterable(postes);
    }
 ///////////////////////////////////////////////////////////////////
    
    
    @DeleteMapping("/tweets/{id}")
    public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String tweetId) {
        return repository.findById(tweetId)
                .flatMap(existingPost
                        -> repository.delete(existingPost)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
