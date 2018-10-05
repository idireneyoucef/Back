package back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}


//	@Bean
//	CommandLineRunner init(ReactiveMongoOperations operations, PostRepository repository) {
//		return args -> {
//			Flux<Post> postFlux = Flux.just(
//					new Post(null, "Big Latte"),
//					new Post(null, "Big Decaf"),
//					new Post(null, "Green Tea"))
//					.flatMap(repository::save);
//
////			postFlux
////					.thenMany(repository.findAll())
////					.subscribe(System.out::println);
//
//             operations.collectionExists(Post.class)
//                    .flatMap(exists -> exists ? operations.dropCollection(Post.class) : Mono.just(exists))
//					.thenMany(v -> operations.createCollection(Post.class))
//					.thenMany(postFlux)
//					.thenMany(repository.findAll())
//					.subscribe(System.out::println);
//		};
//	}
}
