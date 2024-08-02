package com.example.redisproject.common.domain;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter

//@EntityListeners는 JPA에서 엔티티의 생명 주기 이벤트를 관리하는 애노테이션입니다.
//AuditingEntityListener.class는 엔티티 생명 주기 이벤트를 처리하기 위한 리스너 클래스입니다. 여기서는 데이터의 생성일자와 수정일자를 자동으로 관리하기 위해 사용됩니다.
@EntityListeners(value = {AuditingEntityListener.class})

// @MappedSuperclass는 JPA에서 부모 클래스에 사용되는 애노테이션으로, 이 클래스를 상속받는 자식 클래스에서 필드들을 포함하여 테이블을 매핑할 수 있도록 합니다.
// 즉, 이 클래스의 필드들은 데이터베이스 테이블에 직접 매핑되지 않고, 자식 클래스에서 상속받아 사용됩니다.
@MappedSuperclass
public class BaseTimeEntity {


    // @CreatedDate는 Spring Data JPA에서 제공하는 애노테이션으로, 엔티티가 처음으로 저장될 때 자동으로 생성일자를 설정합니다.
    // 이 애노테이션이 붙은 필드는 새로운 엔티티가 데이터베이스에 저장될 때 현재 시간으로 자동 설정됩니다.
    @CreatedDate
    private LocalDateTime createDate;

    // @LastModifiedDate는 Spring Data JPA에서 제공하는 애노테이션으로, 엔티티가 수정될 때마다 자동으로 최종 수정일자를 업데이트합니다.
    // 이 애노테이션이 붙은 필드는 엔티티가 데이터베이스에서 수정될 때마다 현재 시간으로 자동 업데이트됩니다.
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
