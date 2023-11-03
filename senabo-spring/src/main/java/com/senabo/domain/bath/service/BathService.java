package com.senabo.domain.bath.service;

import com.senabo.domain.bath.dto.response.BathResponse;
import com.senabo.domain.bath.entity.Bath;
import com.senabo.domain.bath.repository.BathRepository;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BathService {
    private final BathRepository bathRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BathResponse createBath(Long id) {
        Member member = findById(id);
        Bath bath = bathRepository.save(
                new Bath(member));
        try {
            bathRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return BathResponse.from(bath);
    }

    @Transactional
    public List<Bath> getBath(Long id) {
        Member member = findById(id);
        List<Bath> bathList = bathRepository.findByMemberId(member);
        if (bathList.isEmpty()) {
            throw new EntityNotFoundException("Bath에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return bathList;
    }

    @Transactional
    public void removeBath(Long id) {
        try {
            Member member = findById(id);
            List<Bath> list = bathRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
