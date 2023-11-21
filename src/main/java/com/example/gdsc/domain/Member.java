package com.example.gdsc.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Domain 계층은 DB 내부 실제 테이블과 매핑되는 Java 클래스입니다.
 * 이 클래스는 실제 DB에 저장된 데이터를 갖고 있으며, @Id 어노테이션을 통해 PK를 지정할 수 있고, @Column 어노테이션을 통해 컬럼명을 지정할 수 있습니다.
 * @Entity 어노테이션을 통해 JPA가 이 Domain을 DB에 저장할 수 있도록 합니다.
 * 데이터 무결성을 위해 @Setter는 사용을 지양하며, 정적 생성 메서드를 통해 객체를 생성하고, 수정 메서드를 통해 객체를 수정합니다.
 * 기본생성자의 접근제한자를 protected로 설정하여 오직 생성 메서드만을 통해 객체를 생성하도록 강제합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; // 구분하기 위해 시스템이 저장하는 아이디

    private String name;

    private String email;

    // 생성 메서드
    public static Member createMember(String name, String email) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        return member;
    }

    // 수정 메서드
    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }
}