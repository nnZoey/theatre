package vn.tram.ticket.config;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Caffeine caffeine = jHipsterProperties.getCache().getCaffeine();

        CaffeineConfiguration<Object, Object> caffeineConfiguration = new CaffeineConfiguration<>();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(caffeine.getMaxEntries()));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(TimeUnit.SECONDS.toNanos(caffeine.getTimeToLiveSeconds())));
        caffeineConfiguration.setStatisticsEnabled(true);
        jcacheConfiguration = caffeineConfiguration;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, vn.tram.ticket.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, vn.tram.ticket.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, vn.tram.ticket.domain.User.class.getName());
            createCache(cm, vn.tram.ticket.domain.Authority.class.getName());
            createCache(cm, vn.tram.ticket.domain.User.class.getName() + ".authorities");
            createCache(cm, vn.tram.ticket.domain.AppUser.class.getName());
            createCache(cm, vn.tram.ticket.domain.AppUser.class.getName() + ".orders");
            createCache(cm, vn.tram.ticket.domain.EventType.class.getName());
            createCache(cm, vn.tram.ticket.domain.Event.class.getName());
            createCache(cm, vn.tram.ticket.domain.Event.class.getName() + ".eventTypes");
            createCache(cm, vn.tram.ticket.domain.Event.class.getName() + ".orders");
            createCache(cm, vn.tram.ticket.domain.Seat.class.getName());
            createCache(cm, vn.tram.ticket.domain.Stage.class.getName());
            createCache(cm, vn.tram.ticket.domain.Stage.class.getName() + ".seats");
            createCache(cm, vn.tram.ticket.domain.Stage.class.getName() + ".events");
            createCache(cm, vn.tram.ticket.domain.Ticket.class.getName());
            createCache(cm, vn.tram.ticket.domain.Order.class.getName());
            createCache(cm, vn.tram.ticket.domain.Order.class.getName() + ".tickets");
            // jhipster-needle-caffeine-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
