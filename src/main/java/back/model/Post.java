package back.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class  Post{
    @Id
    private String id;

    private String name;

    private  List<Hashtag> tags;
    
    public Post(){}

    public Post(String id, String name ) {
        this.id = id;
        this.name = name;
    }

    public Post(String id, String name, List<Hashtag> tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Hashtag> getTags() {
        return tags;
    }

    public void setTags(List<Hashtag> tags) {
        this.tags = tags;
    }




    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        return name != null ? name.equals(post.name) : post.name == null;

    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
