package com.senabo.domain.member.service;

import com.senabo.common.api.ApiResponse;
import com.senabo.domain.member.dto.request.SignUpRequest;
import com.senabo.domain.member.dto.request.UpdateInfoRequest;
import com.senabo.domain.member.dto.response.BrushingTeethResponse;
import com.senabo.domain.member.dto.response.CommunicationResponse;
import com.senabo.domain.member.dto.response.MemberResponse;
import com.senabo.domain.member.dto.response.PoopResponse;
import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Communication;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Poop;
import com.senabo.domain.member.repository.BrushingTeethRepository;
import com.senabo.domain.member.repository.CommunicationRepository;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.member.repository.PoopRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserAuthException;
import com.senabo.exception.model.UserException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    private final BrushingTeethRepository brushingTeethRepository;
    private final PoopRepository poopRepository;
    private final CommunicationRepository communicationRepository;

    public boolean checkEmail(String email) {
        boolean duplicateYn = false;
        if (memberRepository.existsByEmail(email)) {
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

    public MemberResponse getInfo(Long id) {
        Member member = findById(id);
        return MemberResponse.from(member);
    }

    public MemberResponse updateInfo(Long id, UpdateInfoRequest request) {
        Member member = findById(id);
        member.update(request);
        return MemberResponse.from(member);
    }


    public BrushingTeethResponse createBrushingTeeth(Long id) {
        Member member = findById(id);
        BrushingTeeth brushingTeeth = brushingTeethRepository.save(
                new BrushingTeeth(member));
        try {
            brushingTeethRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return BrushingTeethResponse.from(brushingTeeth);
    }

    public List<BrushingTeeth> getBrushingTeeth(Long id) {
        List<BrushingTeeth> reponse = brushingTeethsFindByMemberId(id);

        return reponse;
    }

    @Transactional
    public void removeBrushingTeeth(Long id) {
        try {
            Member member = findById(id);
            List<BrushingTeeth> list = brushingTeethRepository.deleteByMemberId(member);

        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }


    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }


    @Transactional
    public List<BrushingTeeth> brushingTeethsFindByMemberId(Long id) {
        Member member = findById(id);
        List<BrushingTeeth> brushingTeethList = brushingTeethRepository.findByMemberId(member);
        if (brushingTeethList.isEmpty()) {
            throw new EntityNotFoundException("Brushing Teeth에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return brushingTeethList;
    }

    @Transactional


    public PoopResponse createPoop(Long id) {
        Member member = findById(id);
        Poop poop = poopRepository.save(
                new Poop(member, false));
        try {
            poopRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return PoopResponse.from(poop);
    }

    //    @Transactional
//    public List<BrushingTeeth> brushingTeethsFindByMemberId(Long id) {
//        Member member = findById(id);
//        List<BrushingTeeth> brushingTeethList = brushingTeethRepository.findByMemberId(member);
//        if (brushingTeethList.isEmpty()) {
//            throw new EntityNotFoundException("Brushing Teeth에서 해당 MemberId를 찾을 수 없습니다.: " + id);
//        }
//        return brushingTeethList;
//    }
    @Transactional
    public List<Poop> getPoop(Long id) {
        Member member = findById(id);
        List<Poop> poopList = poopRepository.findByMemberId(member);
        if (poopList.isEmpty()) {
            throw new EntityNotFoundException("Poop에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return poopList;
    }

    @Transactional
    public void removePoop(Long id) {
        try {
            Member member = findById(id);
            List<Poop> list = poopRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    public CommunicationResponse createCommunication(Long id) {
        Member member = findById(id);
        Communication communication = communicationRepository.save(
                new Communication(member, ));
        try {
            poopRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return PoopResponse.from(poop);
    }

    /**
     * 가장 최신 배변 update
     * @param id
     * @return
     */
//    public PoopResponse updatePoop(Long id) {
//
//        Member member = findById(id);
//
//        member.update(request);
//        return MemberResponse.from(member);
//    }


//    public void userDelete(Member member) {
//        member.getBathList().stream().forEach(bath -> {
//            bath.setMemberId(null);
//        });
//        member.getBrushingTeethList().stream().forEach(brushingTeeth -> {
//            brushingTeeth.setMemberId(null);
//        });
//        member.getCommunicationList().stream().forEach(communication -> {
//            communication.setMemberId(null);
//        });
//        member.getDiseaseList().stream().forEach(disease -> {
//            disease.setMemberId(null);
//        });
//        member.getExpenseList().stream().forEach(expense -> {
//            expense.setMemberId(null);
//        });
//        member.getFeedList().stream().forEach(feed -> {
//            feed.setMemberId(null);
//        });
//        member.getPoopList().stream().forEach(poop -> {
//            poop.setMemberId(null);
//        });
//        member.getStressList().stream().forEach(stress -> {
//            stress.setMemberId(null);
//        });
//        member.getWalkList().stream().forEach(walk -> {
//            walk.setMemberId(null);
//        });
//    }
}
