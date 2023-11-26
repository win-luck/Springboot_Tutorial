package com.example.gdsc;

import com.example.gdsc.dto.MemberDto;
import com.example.gdsc.repository.MemberRepository;
import com.example.gdsc.service.MemberService;
import com.example.gdsc.util.exception.CustomException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() { // 매 테스트 시작 전마다 DB 초기화
        memberRepository.deleteAll();
    }

    @AfterEach
    void tearDown() { // 매 테스트 종료 후마다 DB 초기화
        memberRepository.deleteAll();
    }

    @DisplayName("회원가입 테스트")
    @Test
    void signUpTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");

        // when
        Long id = memberService.join(memberDto);

        // then
        assertEquals(memberDto.getName(), memberRepository.findById(id).get().getName());
    }

    @DisplayName("회원가입 중복 감지 테스트 - USER_ALREADY_EXISTS")
    @Test
    void signUpFailTest() {
        // given
        MemberDto memberDto = MemberDto.of("test1", "email");
        MemberDto memberDto2 = MemberDto.of("test1", "email");

        // when
        memberService.join(memberDto);

        // then
        Assertions.assertThrows(CustomException.class, () -> {
            memberService.join(memberDto2);
        });
    }

    @DisplayName("회원 조회 테스트")
    @Test
    void getMemberTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        Long id = memberService.join(memberDto);

        // when
        MemberDto findMemberDto = memberService.findOne(id);

        // then
        assertEquals(memberDto.getName(), findMemberDto.getName());

    }

    @DisplayName("회원 조회 테스트 - USER_NOT_FOUND")
    @Test
    void getMemberFailTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        Long id = memberService.join(memberDto);
        Long wrongId = id + 1;

        // when
        Assertions.assertThrows(CustomException.class, () -> {
            memberService.findOne(wrongId);
        });
    }

    @DisplayName("회원 목록 조회 테스트")
    @Test
    void getMemberListTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        MemberDto memberDto2 = MemberDto.of("test2", "email");
        memberService.join(memberDto);
        memberService.join(memberDto2);

        // when
        List<MemberDto> members = memberService.findMembers();

        // then
        assertEquals(2, members.size());
        assertEquals(memberDto.getName(), members.get(0).getName());
        assertEquals(memberDto2.getName(), members.get(1).getName());
    }

    @DisplayName("회원 수정 테스트")
    @Test
    void updateMemberTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        Long id = memberService.join(memberDto);

        // when
        memberService.update(id, MemberDto.of("test2", "email2"));

        // then
        assertEquals("test2", memberRepository.findById(id).get().getName());
    }

    @DisplayName("회원 삭제 테스트")
    @Test
    void deleteMemberTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        Long id = memberService.join(memberDto);

        // when
        memberService.delete(id);

        // then
        Assertions.assertThrows(CustomException.class, () -> {
            memberService.findOne(id);
        });
    }
}
