package com.senabo.domain.member.service;

import com.senabo.domain.member.dto.request.CreateExpenseRequest;
import com.senabo.domain.member.dto.request.CreateWalkRequest;
import com.senabo.domain.member.dto.request.UpdateWalkRequest;
import com.senabo.domain.member.dto.response.*;
import com.senabo.domain.member.entity.*;
import com.senabo.domain.member.repository.*;
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
public class ActivityService {
    private final MemberRepository memberRepository;
    private final BrushingTeethRepository brushingTeethRepository;
    private final PoopRepository poopRepository;
    private final CommunicationRepository communicationRepository;
    private final WalkRepository walkRepository;
    private final BathRepository bathRepository;
    private final FeedRepository feedRepository;
    private final ExpenseRepository expenseRepository;
    private final DiseaseRepository diseaseRepository;

    @Transactional
    public BrushingTeethResponse createBrushingTeeth(Long id) {
        Member member = findById(id);
        BrushingTeeth brushingTeeth = brushingTeethRepository.save(
                new BrushingTeeth(member));
        try {
            brushingTeethRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return BrushingTeethResponse.from(brushingTeeth);
    }

    @Transactional
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
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
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
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return PoopResponse.from(poop);
    }


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
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public CommunicationResponse createCommunication(Long id, ActivityType type) {
        Member member = findById(id);
        Communication communication = communicationRepository.save(
                new Communication(member, type));
        try {
            communicationRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return CommunicationResponse.from(communication);
    }

    @Transactional
    public PoopResponse updatePoop(Long id) {
        Member member = findById(id);
        Poop poop = poopRepository.findLatestData(member);
        if (!poop.getCleanYn()) {
            poop.update(true);
        }
        return PoopResponse.from(poop);
    }

    @Transactional
    public List<Communication> getCommunication(Long id) {
        Member member = findById(id);
        List<Communication> communicationList = communicationRepository.findByMemberId(member);
        if (communicationList.isEmpty()) {
            throw new EntityNotFoundException("Communication에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return communicationList;
    }

    @Transactional
    public void removeCommunication(Long id) {
        try {
            Member member = findById(id);
            List<Communication> list = communicationRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public WalkResponse createWalk(Long id, CreateWalkRequest request) {
        Member member = findById(id);
        Walk walk = walkRepository.save(
                new Walk(member, request.startTime(), null, 0.0));
        try {
            walkRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return WalkResponse.from(walk);
    }

    @Transactional
    public List<Walk> getWalk(Long id) {
        Member member = findById(id);
        List<Walk> walkList = walkRepository.findByMemberId(member);
        if (walkList.isEmpty()) {
            throw new EntityNotFoundException("Walk에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return walkList;
    }

    @Transactional
    public WalkResponse updateWalk(Long id, UpdateWalkRequest request) {
        Member member = findById(id);
        Walk walk = walkRepository.findLatestData(member);
        if (walk.getEndTime() == null) {
            walk.update(request.endTime(), request.distnace());
        } else {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_UPDATE_DATA));
        }
        return WalkResponse.from(walk);
    }

    @Transactional
    public void removeWalk(Long id) {
        try {
            Member member = findById(id);
            List<Walk> list = walkRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public ExpenseResponse createExpense(Long id, CreateExpenseRequest request) {
        Member member = findById(id);
        Expense expense = expenseRepository.save(
                new Expense(member, request.item(), request.detail(), request.amount()));
        try {
            expenseRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return ExpenseResponse.from(expense);
    }

    @Transactional
    public List<Expense> getExpense(Long id) {
        Member member = findById(id);
        List<Expense> expenseList = expenseRepository.findByMemberId(member);
        if (expenseList.isEmpty()) {
            throw new EntityNotFoundException("Expense에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return expenseList;
    }

    @Transactional
    public void removeExpense(Long id) {
        try {
            Member member = findById(id);
            List<Expense> list = expenseRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }
    @Transactional
    public FeedResponse createFeed(Long id) {
        Member member = findById(id);
        Feed feed = feedRepository.save(
                new Feed(member));
        try {
            feedRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return FeedResponse.from(feed);
    }

    @Transactional
    public List<Feed> getFeed(Long id) {
        Member member = findById(id);
        List<Feed> feedList = feedRepository.findByMemberId(member);
        if (feedList.isEmpty()) {
            throw new EntityNotFoundException("Feed에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return feedList;
    }

    @Transactional
    public void removeFeed(Long id) {
        try {
            Member member = findById(id);
            List<Feed> list = feedRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public DiseaseResponse createDisease(Long id, String diseaseName) {
        Member member = findById(id);
        Disease disease = diseaseRepository.save(
                new Disease(member,diseaseName));
        try {
            diseaseRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return DiseaseResponse.from(disease);
    }
    @Transactional
    public List<Disease> getDisease(Long id) {
        Member member = findById(id);
        List<Disease> diseaseList = diseaseRepository.findByMemberId(member);
        if (diseaseList.isEmpty()) {
            throw new EntityNotFoundException("Disease에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return diseaseList;
    }

    @Transactional
    public void removeDisease(Long id) {
        try {
            Member member = findById(id);
            List<Disease> list = diseaseRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }
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
