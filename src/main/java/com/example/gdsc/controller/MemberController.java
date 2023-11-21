package com.example.gdsc.controller;

import com.example.gdsc.dto.MemberDto;
import com.example.gdsc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller 계층은 클라이언트를 가장 가까이에서 마주하는 계층입니다.
 * 클라이언트의 요청을 받아서 Service에게 구체적인 비즈니스 로직을 위임하고, Service가 작업을 완료하면 클라이언트에게 응답을 보내주는 계층입니다.
 * REST API를 처리하는 Controller는 @RestController를 사용하고, 일반적인 Controller는 @Controller를 사용합니다.
 * @PathVariable은 URL 경로에 있는 값을 Parameter로 받아올 때 사용합니다.
 * @RequestBody는 HTTP 요청 Body를 Java Object로 변환해주는 어노테이션입니다.
 */
@RequiredArgsConstructor // 의존성 주입을 위해 Service에 final을 붙이고 @RequiredArgsConstructor를 붙여줍니다.
@RestController // @Controller와 @ResponseBody를 합친 것으로, JSON 형태로 데이터를 반환할 수 있습니다.
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/members/new")
    public ResponseEntity<Long> create(@RequestBody MemberDto memberDto){
        return ResponseEntity.ok(memberService.join(memberDto));
    }

    // 회원 수정
    @PutMapping("/members/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MemberDto memberDto){
        memberService.update(id, memberDto);
        return ResponseEntity.ok().build();
    }

    // 특정 회원 조회
    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id){
        return ResponseEntity.ok(memberService.findOne(id));
    }

    // 전체 회원 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberDto>> getAllUsers(){
        return ResponseEntity.ok(memberService.findMembers());
    }
}
