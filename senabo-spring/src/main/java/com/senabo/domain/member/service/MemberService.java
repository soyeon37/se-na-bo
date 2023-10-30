package com.senabo.domain.member.service;

import com.senabo.domain.member.dto.request.SignUpRequest;
import com.senabo.domain.member.dto.response.MemberResponse;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserAuthException;
import com.senabo.exception.model.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public boolean checkEmail(String email) {
        boolean duplicateYn = false;
        if(memberRepository.existsByEmail(email)){
            duplicateYn = true;
        }
        return duplicateYn;
    }

    public MemberResponse signUp(SignUpRequest request) {
        Member member = memberRepository.save(
                new Member(request.name(), request.email(), request.species(), request.sex(), request.houseLatitude(), request.houseLogitude(), request.snsType()));
        try {
            memberRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .species(member.getSpecies())
                .sex(member.getSex())
                .houseLatitude(member.getHouseLatitude())
                .houseLogitude(member.getHouseLogitude())
                .snsType(member.getSnsType())
                .affection(member.getAffection())
                .stressLevel(member.getStressLevel())
                .totalTime(member.getTotalTime())
                .exitTime(member.getExitTime())
                .enterTime(member.getEnterTime())
                .build();
    }

    @Transactional
    public void removeMember(Long id) {
        try {
//            refreshTokenService.delValues(request.refreshToken());
            Member member = findById(id);
            memberRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }


}
