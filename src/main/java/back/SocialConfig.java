package back;

import back.repository.PostRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import javax.inject.Inject;

@Configuration
public class SocialConfig {

    @Inject
    private Environment environment;
    String consumerKey = "aCY9VL94qOjfCDrcUNmawIMPm"; // The application's consumer key
    String consumerSecret = "xmC2vaq5tb5BFQHdPKb3W2NAx5f71zKbfd8CuELzvPuNdAG26s"; // The application's consumer secret
    String accessToken = "2574519362-mvAPOMWBsr9NuwuqLy3Se8zYVCYjAufG24R46fe"; // The access token granted after OAuth authorization
    String accessTokenSecret = "Qx1YWFMZATbXmCqxBlsKUnTvBRLJbPPVUKUoQInRe2GAv"; // The access token secret granted after OAuth authorization



    @Bean
     public Twitter getTwitter(){
        return   new TwitterTemplate(consumerKey,consumerSecret);
    }




}