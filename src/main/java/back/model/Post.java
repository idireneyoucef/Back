package back.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class  Post{
    @Id
    private String id;
    
    @JsonProperty("text")
    @NotBlank
    private String text;
    
    
    @JsonProperty("createdAt")
    @NotNull
    private Date createdAt;
    
    private  List<HashTagEntity> tags; 
    
    @JsonProperty("user")
    private TwitterProfile user;


}
