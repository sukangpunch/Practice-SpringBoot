package com.example.redisproject.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//Spring에게 이 클래스가 Bean 구성에 필요한 설정을 포함하고 있다는 것을 알려줍니다.
@Configuration
//Spring Data JPA에서 자동 감사(Auditing) 기능을 활성화하는 데 사용됩니다.
@EnableJpaAuditing
public class JpaAuditingConfig {
}
