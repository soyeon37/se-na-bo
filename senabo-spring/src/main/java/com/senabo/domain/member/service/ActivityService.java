package com.senabo.domain.member.service;

import com.senabo.domain.member.dto.request.CreateExpenseRequest;
import com.senabo.domain.member.dto.request.CreateWalkRequest;
import com.senabo.domain.member.dto.request.UpdateWalkRequest;
import com.senabo.domain.member.dto.response.*;
import com.senabo.domain.member.entity.*;
import com.senabo.domain.member.repository.*;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import jakarta.persistence.AccessType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    private final StressRepository stressRepository;
    private final AffectionRepository affectionRepository;

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
                new Disease(member, diseaseName));
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
    public Map<String, Object> checkLastFeed(Long id) {
        Map<String, Object> result = new HashMap<>();
        Member member = findById(id);
        Feed feed = feedRepository.findLatestData(member);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowH = now.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime lastFeedH = feed.getCreateTime().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime twelveAfter = lastFeedH.plusHours(12);
        if (nowH.isBefore(twelveAfter)) {
            result.put("possibleYn", false);
        } else {
            result.put("possibleYn", true);
        }
        result.put("lastFeedDateTime", lastFeedH);
        result.put("nowDateTime", nowH);
        return result;
    }


    @Transactional
    public void scheduleFeed(Long id) {
        Member member = findById(id);
        Feed feed = feedRepository.findLatestData(member);
        int originStress = member.getStressLevel();

        if (feed == null || originStress == 100) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowH = now.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime lastFeedH = feed.getCreateTime().truncatedTo(ChronoUnit.HOURS);

        LocalDateTime twelveAfter = lastFeedH.plusHours(12);
        LocalDateTime fifteenAfter = lastFeedH.plusHours(15);

        log.info("현재 시각: " + nowH);
        log.info("마지막으로 먹이준 시간: " + lastFeedH);
        log.info("마지막으로 먹이준 시간 + 12: " + twelveAfter);


        // 배식 12시간 경과
        if (nowH.equals(twelveAfter)) {
            log.info("배식 후 12시간 경과: 밥 푸시 알림 실행");
            // 밥 푸시 알림
            /*

             */
        }
        // 배식 13시간 경과 이후 : 스트레스 1 씩 증가
        else if (nowH.isAfter(twelveAfter)) {
            log.info("배식 후 13시간 경과: 스트레스 증가");
            // 스트레스 1 증가
            int newStress = originStress + 1;
            Duration duration = Duration.between(nowH, lastFeedH);
            long hours = duration.toHours();
            String detail = "배식 "+ hours +"시간 경과했습니다. 스트레스 + 1";
            int changeAmount = 1;
            // 배식 15시간 경과 : 스트레스 3 증가 (1회)
            if (nowH.isEqual(fifteenAfter)) {
                log.info("배식 "+ hours +"시간 경과: 공복 토 푸시 알림 및 스트레스 3 증가");
                // 공복 토 푸시 알림
                /*

                 */
                // 스트레스 3 증가
                newStress = originStress + 3;
                detail = "배식 "+ hours +"시간 경과로 공복 토를 했습니다. 스트레스 + 3";
                changeAmount = 3;
            }
            Stress stress = stressRepository.save(
                    new Stress(member, StressType.FEED, detail, changeAmount, newStress)
            );
            member.updateStress(newStress);
            try {
                stressRepository.flush();
            } catch (DataIntegrityViolationException e) {
                throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
            }
            StressResponse.from(stress);
        }

    }

    @Transactional
    public void schedulePoop(Long id) {
        Member member = findById(id);
        int originStress = member.getStressLevel();
        Poop poop = poopRepository.findLatestData(member);

        if(originStress == 100 || poop.getCleanYn()) return;

        Map<String, Object> map = checkLastFeed(1L);
        LocalDateTime lastFeedH = (LocalDateTime) map.get("lastFeedDateTime");
        LocalDateTime nowH = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        log.info("마지막 밥 제공 시간: "+lastFeedH);
        LocalDateTime threeAfter = lastFeedH.plusHours(3);
        if(nowH.isEqual(threeAfter)){
            // 배변 활동 푸시 알림
            /*

             */
        }else if(nowH.isAfter(threeAfter)){
            // 스트레스 1 증가
            Duration duration = Duration.between(nowH, lastFeedH);
            long hours = duration.toHours();
            log.info("배변 후 "+ hours +"시간 경과: 스트레스 1 증가");
            int newStress = originStress + 1;
            String detail = "배변 패드를 치우지 않은 채 "+ hours +"시간이 경과했습니다. 스트레스 + 1";
            int changeAmount = 1;

            Stress stress = stressRepository.save(
                    new Stress(member, StressType.POOP, detail, changeAmount, newStress)
            );
            member.updateStress(newStress);
            try {
                stressRepository.flush();
            } catch (DataIntegrityViolationException e) {
                throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
            }
            StressResponse.from(stress);
        }
    }


    // 매일 오후 12시, 20시에 실행
    @Scheduled(cron = "0 0 12,20 * * *")
    public void scheduleWalk(){
        // @AuthenticationPrincipal UserDetails userDetails
        // Member member = findById(principal.getUsername());
        Member member = findById(1L);

        // 산책 푸시 알림
        /*

         */
    }

    @Transactional
    public void scheduleCheckWalk(Long id){
        Member member = findById(id);
        int originAffection = member.getAffection();
        int originStress = member.getStressLevel();
        int newAffection = 0;
        int newStress = 0;
        String affectionDetail = "";
        String stressDetail = "";
        int changeAffectionAmount = 0;
        int changeStressAmount = 0;

        LocalDateTime startToday = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<Walk> list = walkRepository.findTodayData(member, startToday);
        double totalDistance = 0.0;
        for(int i = 0; i < list.size(); i++){
             totalDistance += list.get(i).getDistance();
        }

        log.info("총 산책 거리: "+totalDistance);

        boolean complete = false;

        if(totalDistance < 5.0){

            affectionDetail = "오늘의 총 산책 거리는 "+ totalDistance +"Km 입니다. 애정 - 5";
            newAffection = originAffection - 5;
            changeAffectionAmount = 5;

            stressDetail = "오늘의 총 산책 거리는 "+ totalDistance +"Km 입니다. 스트레스 + 10";
            newStress = originStress + 10;
            changeStressAmount = 10;
        }else{
            complete = true;
            affectionDetail = "오늘의 총 산책 거리는 "+ totalDistance +"Km 입니다. 애정 + 5";
            changeAffectionAmount = 5;
            newAffection = originAffection + 5;

            stressDetail = "오늘의 총 산책 거리는 "+ totalDistance +"Km 입니다. 스트레스 - 10";
            newStress = originStress - 10;
            changeStressAmount = 10;
        }


        // !complete 미완료 + stress 증가 -> not 100
        // complete 완료 + stress 감소 -> not 0
        if((!complete && originStress != 100) || (complete && originStress != 0)) {
            log.info("스트레스 저장");
            Stress stress = stressRepository.save(
                    new Stress(member, StressType.WALK, stressDetail, changeStressAmount, newStress)
            );
            member.updateStress(newStress);
            try {
                stressRepository.flush();
            } catch (DataIntegrityViolationException e) {
                throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
            }
            StressResponse.from(stress);
        }


        // 미완료 + affection 감소 -> not 0
        // 완료 + affection 증가 -> not 100
        if((!complete && originAffection != 0) || (complete && originAffection != 100)) {
            log.info("애정 저장");
            Affection affection = affectionRepository.save(
                    new Affection(member, AffectionType.WALK, affectionDetail, changeAffectionAmount, newAffection)
            );
            member.updateAffection(newAffection);
            try {
                stressRepository.flush();
                affectionRepository.flush();
            } catch (DataIntegrityViolationException e) {
                throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
            }
            AffectionResponse.from(affection);
        }



    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
