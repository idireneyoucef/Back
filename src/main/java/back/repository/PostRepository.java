package back.repository;

import back.model.Post;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface PostRepository
        extends ReactiveMongoRepository<Post, String> {

//    @Query("{ 'description' : ?0 }")
//    Flux<Post> findPostByDescription(@Param("description") String description);
    
//    @Query("{ 'description' : ?0 }") 
    @Query("{ 'name' : { $regex: ?0 } }")
    public Flux<Post> findPostByName(String name);
    
    
    @Query("{ 'tags' :{'tag' : ?0}}") 
    public Flux<Post> findPostByTag(String tag);
}
