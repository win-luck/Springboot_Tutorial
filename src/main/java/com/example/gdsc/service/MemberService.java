package com.example.gdsc.service;

import com.example.gdsc.domain.Member;
import com.example.gdsc.dto.MemberDto;
import com.example.gdsc.repository.MemberRepository;
import com.example.gdsc.util.api.ResponseCode;
import com.example.gdsc.util.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Service 계층은 CRUD를 비롯한 크고 작은 비즈니스 로직을 처리하는 계층입니다.
 * Controller로부터 넘어온 요청을 처리하고, Repository 계층을 통해 DB에 접근하여 요청된 작업을 수행합니다.
 * Service 계층은 Controller와 Repository 계층 사이에서 중간 역할을 수행하는 Springboot에서 가장 중요한 계층입니다.
 * 뿐만 아니라 Entity to Dto나 Dto to Entity와 같은 객체 변환 작업도 일반적으로 Service 계층에서 수행합니다. (그렇지 않은 경우도 있습니다.)
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원 가입
     * 생성 메서드를 통해 회원 객체를 생성하고, 이를 DB에 저장합니다.
     * 실제 서비스를 만들 때는 특정 값의 중복 여부 등을 판정해야 하는 상황이 존재할 수 있기에 이를 위한 메서드를 별도로 만들어서 사용하는 경우가 많습니다.
     * */
    public Long join(MemberDto memberDto){
        validateDuplicateMember(memberDto.getName());
        Member member = Member.createMember(memberDto.getName(), memberDto.getEmail()); // 멤버 객체 생성
        return memberRepository.save(member).getId(); // 멤버 객체 DB 테이블에 저장하고, 이 멤버의 고유식별자 반환
    }

    /**
     * 중복 회원 검증용 메서드
     * 이름을 통해 중복 여부를 검증하는 내부 메서드이기에, private을 사용했습니다.
     * 존재 여부만 판정할 때는 existsBy 메서드를 사용하는 것이 서버 리소스를 아낄 수 있습니다.
     * 그러나 실제로 member 객체를 가져와야 하는 경우에는 findBy 메서드를 사용해야 합니다.
     * 만약 직접 객체를 가져와야 하는 경우엔 validate보다는 getMemberBy~와 같은 메서드명으로 작성하는 것이 좋습니다.
     */
    private void validateDuplicateMember(String name) {
        if(memberRepository.existsByName(name)){
            throw new CustomException(ResponseCode.USER_ALREADY_EXIST);
        }
        /*memberRepository.findByName(name).ifPresent(m ->{
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });*/
    }

    /**
     * 회원 수정
     */
    public void update(Long memberId, MemberDto memberDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));
        member.update(memberDto.getName(), memberDto.getEmail());
        memberRepository.save(member);
    }

    /**
     * 전체 회원 조회
     * findAll()은 특정 테이블에 존재하는 모든 데이터를 조회하는 메서드입니다.
     * 다만 모든 데이터를 가져오는 만큼 서버에 부하가 걸리기 때문에, 실제로 사용할 때는 페이징(10개씩) 처리를 해서 사용하는 것이 좋습니다.
     * 페이징의 경우에는 추후 기회가 된다면 다루도록 하겠습니다.
     * */
    public List<MemberDto> findMembers(){
        List<Member> members = memberRepository.findAll();
        /*List<MemberDto> memberDtos = new ArrayList<>();
        for(Member member : members){
            memberDtos.add(MemberDto.from(member));
        }
        return memberDtos;*/
        // 객체를 Dto로 바꾸는 위 for문은 아래 stream을 사용한 코드와 같은 결과를 반환합니다. stream을 통해 코드를 간결하게 만들 수 있습니다.
        return members.stream().map(MemberDto::from).collect(Collectors.toList());
    }

    /**
     * 회원 한명 조회 (Optional)
     * Optional은 null이 반환될 가능성이 있는 객체를 감싸서 NullPointException을 방지하기 위해 사용합니다.
     * 다만 Optional을 사용하면 성능이 느려질 수 있기에, 실전에서는 Optional 없이 orElseThrow()를 사용하는 경우가 많습니다.
     * */
    public Optional<MemberDto> findOneOptional(Long memberId){
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        // Optional.map()은 Optional 객체가 존재할 경우에만 실행되며, 존재하지 않을 경우에는 실행되지 않고 빈 Optional 객체를 반환합니다.
        return memberOpt.map(MemberDto::from);
    }

    /**
     * 회원 한명 조회 (Optional 없이 orElseThrow() 사용)
     * orElseThrow() 메서드를 사용하면, 값이 비어있는 경우에는 예외처리를 이끌어냅니다.
     * Optional의 빈 값을 반환하는 것이 의미가 없다면, 이 방식으로 예외처리를 간결하게 할 수 있습니다.
     * 다만 실제로 findBy를 통해 Repository 계층에서 뽑아내는 값 자체는 항상 Optional로 감싸져 있습니다.
     * */
    public MemberDto findOne(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));
        return MemberDto.from(member);
    }

    /**
     * 회원 삭제
     */
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
