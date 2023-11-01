package com.senabo.domain.member.service;

import com.senabo.domain.member.dto.request.SignUpRequest;
import com.senabo.domain.member.dto.request.UpdateInfoRequest;
import com.senabo.domain.member.dto.response.MemberResponse;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Report;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.member.repository.ReportRepository;
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
    private final ReportRepository reportRepository;


    public boolean checkEmail(String email) {
        boolean duplicateYn = false;
        if (memberRepository.existsByEmail(email)) {
            duplicateYn = true;
        }
        return duplicateYn;
    }

    public MemberResponse signUp(SignUpRequest request) {
        // 회원가입
        Member member = memberRepository.save(
                new Member(request.name(), request.email(), request.species(), request.sex(), request.houseLatitude(), request.houseLogitude(), request.snsType()));

        Report report = reportRepository.save(
                new Report(member, 1, 0, 50)
        );
        try {
            memberRepository.flush();
            reportRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }


        return MemberResponse.from(member);
    }

    @Transactional
    public void removeMember(Long id) {
        try {
//            refreshTokenService.delValues(request.refreshToken());
            Member member = findById(id);
//            userDelete(member);
            memberRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public MemberResponse getInfo(Long id) {
        Member member = findById(id);
        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateInfo(Long id, UpdateInfoRequest request) {
        Member member = findById(id);
        member.update(request);
        return MemberResponse.from(member);
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }


}
