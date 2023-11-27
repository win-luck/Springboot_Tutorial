package com.example.gdsc;

import com.example.gdsc.domain.Member;
import com.example.gdsc.dto.MemberDto;
import com.example.gdsc.repository.MemberRepository;
import com.example.gdsc.service.MemberService;
import com.example.gdsc.util.api.ResponseCode;
import com.example.gdsc.util.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberMockServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @DisplayName("회원가입 테스트")
    @Test
    void signUpTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        Member member = Member.createMember(memberDto.getName(), memberDto.getEmail());
        member.setId(1L);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        Long id = memberService.join(memberDto);

        // then
        assertEquals(1L, id);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @DisplayName("회원가입 중복 감지 테스트 - USER_ALREADY_EXISTS")
    @Test
    void signUpFailTest() {
        // given
        MemberDto memberDto = MemberDto.of("test1", "email");
        given(memberRepository.existsByName(any(String.class))).willReturn(true);

        // when
        Assertions.assertThrows(CustomException.class, () -> {
            memberService.join(memberDto);
        });
    }

    @DisplayName("회원 조회 테스트")
    @Test
    void getMemberTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        Member member = Member.createMember(memberDto.getName(), memberDto.getEmail());
        member.setId(1L);
        given(memberRepository.findById(any(Long.class))).willReturn(java.util.Optional.of(member));

        // when
        MemberDto findMemberDto = memberService.findOne(1L);

        // then
        assertEquals(memberDto.getName(), findMemberDto.getName());
        assertEquals(memberDto.getEmail(), findMemberDto.getEmail());
        verify(memberRepository, times(1)).findById(any(Long.class));
    }

    @DisplayName("회원 조회 테스트 - USER_NOT_FOUND")
    @Test
    void getMemberFailTest() {
        // given
        given(memberRepository.findById(1L)).willThrow(new CustomException(ResponseCode.USER_NOT_FOUND));

        // when
        Assertions.assertThrows(CustomException.class, () -> {
            memberService.findOne(1L);
        });
    }

    @DisplayName("회원 목록 조회 테스트")
    @Test
    void getMemberListTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        MemberDto memberDto2 = MemberDto.of("test2", "email");
        given(memberRepository.findAll()).willReturn(List.of(
                Member.createMember(memberDto.getName(), memberDto.getEmail()),
                Member.createMember(memberDto2.getName(), memberDto2.getEmail())
        ));

        // when
        List<MemberDto> members = memberService.findMembers();

        // then
        assertEquals(2, members.size());
        assertEquals(memberDto.getName(), members.get(0).getName());
        assertEquals(memberDto2.getName(), members.get(1).getName());
        verify(memberRepository, times(1)).findAll();
    }

    @DisplayName("회원 수정 테스트")
    @Test
    void updateMemberTest() {
        // given
        MemberDto memberDto = MemberDto.of("test", "email");
        MemberDto updateMemberDto = MemberDto.of("test2", "email2");
        Member member = Member.createMember(memberDto.getName(), memberDto.getEmail());
        Member updateMember = Member.createMember("test2", "email2");
        member.setId(1L);
        given(memberRepository.findById(any(Long.class))).willReturn(java.util.Optional.of(member));
        given(memberRepository.save(any(Member.class))).willReturn(updateMember);

        // when
        memberService.update(1L, updateMemberDto);

        // then
        assertEquals("test2", memberRepository.findById(1L).get().getName());
    }

    @DisplayName("회원 삭제 테스트")
    @Test
    void deleteMemberTest() {
        // when
        memberService.delete(1L);

        // then
        verify(memberRepository, times(1)).deleteById(any(Long.class));
    }
}
