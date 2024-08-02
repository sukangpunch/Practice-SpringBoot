package querydsl.querydslapi.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    //엔터프라이즈 자바 빈 컨테이너에서 엔티티 매니저 인스턴스를 주입할 떄 사용
    //jpa에서 관리되는 영속성 컨텍스트에 엑세스 할 수 있도록 해준다.
    //따라서 해당 어노테이션을 사용하여 엔티티 매니저를 주입하면 JPA 를 사용하여 데이터베이스와 상호작용이 가능하다.
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory queryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
