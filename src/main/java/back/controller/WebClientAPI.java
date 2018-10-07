package back.controller;

 
import back.model.Post;
import back.model.PostEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebClientAPI {
    private WebClient webClient;

    WebClientAPI() {
        //this.webClient = WebClient.create("http://localhost:8080/posts");
        this.webClient =WebClient.builder()
                .baseUrl("http://localhost:8080/posts")
                .build();
    }

    public static void main(String args[]) {
        WebClientAPI api = new WebClientAPI();

        api.postNewPost()
                .thenMany(api.getAllPosts())
                .take(1)                  
                .thenMany(api.getAllPosts())
                .thenMany(api.getAllEvents())
                .subscribe(System.out::println);
    }

    private Mono<ResponseEntity<Post>> postNewPost() {
        return webClient
                .post()
                .body(Mono.just(new Post()), Post.class)
                .exchange()
                .flatMap(response -> response.toEntity(Post.class))
                .doOnSuccess(o -> System.out.println("**********POST " + o));
    }

    private Flux<Post> getAllPosts() {
        return webClient
                .get()
                .retrieve()
                .bodyToFlux(Post.class)
                .doOnNext(o -> System.out.println("**********GET: " + o));
    }

    private Mono<Post> updatePost(String id, String name, double price) {
        return webClient
                .put()
                .uri("/{id}", id)
                .body(Mono.just(new Post()), Post.class)
                .retrieve()
                .bodyToMono(Post.class)
                .doOnSuccess(o -> System.out.println("**********UPDATE " + o));
    }

    private Mono<Void> deletePost(String id) {
        return webClient
                .delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(o -> System.out.println("**********DELETE " + o));
    }

    private Flux<PostEvent> getAllEvents() {
        return webClient
                .get()
                .uri("/events")
                .retrieve()
                .bodyToFlux(PostEvent.class);
    }
}
