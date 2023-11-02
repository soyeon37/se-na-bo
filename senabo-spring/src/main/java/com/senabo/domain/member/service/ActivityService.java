package com.senabo.domain.member.service;

import com.senabo.domain.member.dto.request.CreateExpenseRequest;
import com.senabo.domain.member.dto.request.CreateWalkRequest;
import com.senabo.domain.member.dto.request.UpdateTotalTimeRequest;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final StressRepository stressRepository;
    private final AffectionRepository affectionRepository;
    private final ReportRepository reportRepository;


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
        Member member = findById(id);

        List<BrushingTeeth> brushingTeethList = brushingTeethRepository.findByMemberId(member);
        if (brushingTeethList.isEmpty()) {
            throw new EntityNotFoundException("Brushing Teeth에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return brushingTeethList;
    }
    @Transactional
    public List<BrushingTeeth> getBrushingTeethWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<BrushingTeeth> brushingTeethList = brushingTeethRepository.findBrushingTeethWeek(member, endTime, startTime);
        if (brushingTeethList.isEmpty()) {
            throw new EntityNotFoundException("BrushingTeeth에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return brushingTeethList;
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
    public List<Poop> getPoopWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Poop> poopList = poopRepository.findPoopWeek(member, endTime, startTime);
        if (poopList.isEmpty()) {
            throw new EntityNotFoundException("Poop에서 해당 주차를 찾을 수 없습니다.: " + id);
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
    public List<Communication> getCommunicationWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Communication> communicationList = communicationRepository.findCommunicationWeek(member, endTime, startTime);
        if (communicationList.isEmpty()) {
            throw new EntityNotFoundException("Communication에서 해당 주차를 찾을 수 없습니다.: " + id);
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
    public List<Walk> getWalkWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Walk> walkList = walkRepository.findWalkWeek(member, endTime, startTime);
        if (walkList.isEmpty()) {
            throw new EntityNotFoundException("Walk에서 해당 주차를 찾을 수 없습니다.: " + id);
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
    public Long getExpenseTotal(Long id) {
        Member member = findById(id);
        Long totalAmount = expenseRepository.getTotalAmount(member);
       return totalAmount;
    }

    @Transactional
    public Long getExpenseTotalWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        Long totalAmount = expenseRepository.getTotalAmountWeek(member, endTime, startTime);
        return totalAmount;
    }


    @Transactional
    public List<Expense> getExpenseWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Expense> expenseList = expenseRepository.findExpenseWeek(member, endTime, startTime);
        if (expenseList.isEmpty()) {
            throw new EntityNotFoundException("Expense에서 해당 주차를 찾을 수 없습니다.: " + id);
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
    public List<Feed> getFeedWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Feed> feedList = feedRepository.findFeedWeek(member, endTime, startTime);
        if (feedList.isEmpty()) {
            throw new EntityNotFoundException("Feed에서 해당 주차를 찾을 수 없습니다.: " + id);
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
    public List<Disease> getDiseaseWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Disease> diseaseList = diseaseRepository.findDiseaseWeek(member, endTime, startTime);
        if (diseaseList.isEmpty()) {
            throw new EntityNotFoundException("Disease에서 해당 주차를 찾을 수 없습니다.: " + id);
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
    public List<Report> getReport(Long id) {
        Member member = findById(id);
        List<Report> reportList = reportRepository.findByMemberId(member);
        if(reportList.isEmpty()){
            throw new EntityNotFoundException("Report에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return reportList;
    }

    @Transactional
    public List<Report> getReportWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Report> reportList = reportRepository.findReportWeek(member, endTime, startTime);
        if(reportList.isEmpty()){
            throw new EntityNotFoundException("Report에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return reportList;
    }

    // 앱 사용 시간 저장
    @Transactional
    public ReportResponse updateTotalTime(Long id, UpdateTotalTimeRequest request){
        Member member = findById(id);
        Duration duration = Duration.between(request.startTime(), request.endTime());
        int hour = (int) duration.toHours();
        Report report = reportRepository.findLatestData(member);
        report.updateTotalTime(hour);
        return  ReportResponse.from(report);
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
            Duration duration = Duration.between(nowH, lastFeedH);
            long hours = duration.toHours();
            int changeAmount = 1;
            // 배식 15시간 경과 : 스트레스 3 증가 (1회)
            if (nowH.isEqual(fifteenAfter)) {
                log.info("배식 " + hours + "시간 경과: 공복 토 푸시 알림 및 스트레스 3 증가");
                // 공복 토 푸시 알림
                /*

                 */
                // 스트레스 3 증가
                changeAmount = 3;
            }
            saveStress(member, StressType.FEED, changeAmount);
        }

    }

    @Transactional
    public void schedulePoop(Long id) {
        Member member = findById(id);
        int originStress = member.getStressLevel();
        Poop poop = poopRepository.findLatestData(member);

        if (originStress == 100 || poop.getCleanYn()) return;

        Map<String, Object> map = checkLastFeed(1L);
        LocalDateTime lastFeedH = (LocalDateTime) map.get("lastFeedDateTime");
        LocalDateTime nowH = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        log.info("마지막 밥 제공 시간: " + lastFeedH);
        LocalDateTime threeAfter = lastFeedH.plusHours(3);
        if (nowH.isEqual(threeAfter)) {
            // 배변 활동 푸시 알림
            /*

             */
        } else if (nowH.isAfter(threeAfter)) {
            // 스트레스 1 증가
            Duration duration = Duration.between(nowH, lastFeedH);
            long hours = duration.toHours();
            log.info("배변 후 " + hours + "시간 경과: 스트레스 1 증가");
            int changeAmount = 1;

            saveStress(member, StressType.POOP, changeAmount);
        }
    }




    @Transactional
    public void scheduleCheckWalk(Long id) {
        Member member = findById(id);
        int originAffection = member.getAffection();
        int originStress = member.getStressLevel();
        int changeAffectionAmount = 0;
        int changeStressAmount = 0;

        LocalDateTime startToday = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<Walk> list = walkRepository.findTodayData(member, startToday);
        double totalDistance = 0.0;
        for (int i = 0; i < list.size(); i++) {
            totalDistance += list.get(i).getDistance();
        }

        log.info("총 산책 거리: " + totalDistance);

        boolean complete = false;

        if (totalDistance < 5.0) {
            changeAffectionAmount = -5;
            changeStressAmount = 10;
        } else {
            complete = true;
            changeAffectionAmount = 5;
            changeStressAmount = -10;
        }


        // !complete 미완료 + stress 증가 -> not 100
        // complete 완료 + stress 감소 -> not 0
        if ((!complete && originStress != 100) || (complete && originStress != 0)) {
            saveStress(member, StressType.WALK, changeStressAmount);
        }


        // 미완료 + affection 감소 -> not 0
        // 완료 + affection 증가 -> not 100
        if ((!complete && originAffection != 0) || (complete && originAffection != 100)) {
            saveAffection(member, AffectionType.WALK, changeAffectionAmount);
        }
    }
    @Transactional
    public StressResponse saveStress(Member member, StressType type, int changeAmount){
        log.info("스트레스 저장");
        int originStress = member.getStressLevel();
        Stress stress = stressRepository.save(
                new Stress(member, type, changeAmount, originStress + changeAmount)
        );
        member.updateStress(originStress + changeAmount);
        try {
            stressRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return StressResponse.from(stress);
    }

    @Transactional
    public AffectionResponse saveAffection(Member member, AffectionType type, int changeAmount) {
        log.info("애정 저장");
        int originAffection = member.getAffection();
        Affection affection = affectionRepository.save(
                new Affection(member, type, changeAmount, originAffection+changeAmount)
        );
        member.updateAffection(originAffection+changeAmount);
        try {
            affectionRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return AffectionResponse.from(affection);
    }

    public AffectionResponse createAffection(Long id, AffectionType type, int changeAmount) {
        Member member = findById(id);
        return saveAffection(member, type, changeAmount);
    }

    @Transactional
    public List<Affection> getAffection(Long id) {
        Member member = findById(id);
        List<Affection> affectionList = affectionRepository.findByMemberId(member);
        if (affectionList.isEmpty()) {
            throw new EntityNotFoundException("Affection에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return affectionList;
    }

    @Transactional
    public List<Affection> getAffectionWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Affection> affectionList = affectionRepository.findAffectionWeek(member, endTime, startTime);
        if (affectionList.isEmpty()) {
            throw new EntityNotFoundException("Affection에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return affectionList;
    }
    @Transactional
    public StressResponse createStress(Long id, StressType type, int changeAmount) {
        Member member = findById(id);
        return saveStress(member, type, changeAmount);
    }

    @Transactional
    public List<Stress> getStress(Long id) {
        Member member = findById(id);
        List<Stress> stressList = stressRepository.findByMemberId(member);
        if (stressList.isEmpty()) {
            throw new EntityNotFoundException("Stress에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return stressList;
    }
    @Transactional
    public List<Stress> getStressWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Stress> stressList = stressRepository.findStressWeek(member, endTime, startTime);
        if (stressList.isEmpty()) {
            throw new EntityNotFoundException("Stress에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return stressList;
    }

    @Transactional
    public void scheduleReport(Long id) {
        // 마지막 리포트의 create datetime 가져오기
        Member member = findById(id);
        Report lastReport = reportRepository.findLatestData(member);
        LocalDateTime lastCreateTime = lastReport.getCreateTime();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastStart = lastCreateTime.truncatedTo(ChronoUnit.DAYS);
        // 7일이 되었으면 새로운 report 생성
        Duration duration = Duration.between(now, lastCreateTime);
        long days = duration.toDays();
        if (days >= 7 && !lastReport.getComplete()) {
            /**
             * Affection
             */
            Affection affection = getLatestAffectionData(member);
            int endAffectionScore = affection.getScore();
            /**
             * Stress
             */
            Stress stress = getLatestStressData(member);
            int endStressScore = stress.getScore();
            /**
             * 배변 score
             * Poop 테이블에서 lastCreateTime 기준보다 큰 data를 가져온다.
             * 배변 패드를 안 치운 횟수를 100에서 감점한다.
             * poopList의 전체 횟수 - 치우지 않은 횟수 / 168 * 100
             */
            Long poopStressCnt = 0L;
            poopStressCnt = getCountLastWeekList(member, lastStart, StressType.POOP);
            poopStressCnt = getCountLastWeekList(member, lastStart, StressType.POOP);
            int poopScore = (int) (1 - poopStressCnt / 168) * 100;

            /**
             *  산책 score
             */
            Long walkStressCnt = 0L;
            walkStressCnt = getCountLastWeekList(member, lastStart, StressType.WALK);
            int walkScore = (int) (1 - walkStressCnt / 7) * 100;

            // 질병 score
            Long diseaseStressCnt = 0L;
            diseaseStressCnt = getCountLastWeekDiseaseList(member, lastStart);
            int diseaseScore = (int) (100 - diseaseStressCnt * 10);
            if (diseaseScore <= 0) diseaseScore = 0;

            // 먹이 score
            Long feedStressCnt = 0L;
            feedStressCnt = getCountLastWeekList(member, lastStart, StressType.FEED);
            int feedScore = (int) (1 - feedStressCnt / 14) * 100;

            // 양치 score
            Long brusingStressCnt = 0L;
            brusingStressCnt = getCountLastWeekList(member, lastStart, StressType.BRUSHING_TEETH);
            int brushingScore = (int) (1 - brusingStressCnt / 7) * 100;

            // lastreport update
            lastReport.update(endAffectionScore, endStressScore, poopScore, walkScore, feedScore, brushingScore, diseaseScore);
            ReportResponse.from(lastReport);

            // newreport save
            Report newReport = reportRepository.save(
                    new Report(member, lastReport.getWeek()+1, endAffectionScore, endStressScore)
            );
            try {
              reportRepository.flush();
            } catch (DataIntegrityViolationException e) {
                throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
            }

        }
    }


    @Transactional
    public Affection getLatestAffectionData(Member member) {
        return affectionRepository.findLatestData(member);
    }

    @Transactional
    public Stress getLatestStressData(Member member) {
        return stressRepository.findLatestData(member);
    }

    @Transactional
    public List<Stress> getLastWeekList(Member member, LocalDateTime lastStart, StressType type) {
        List<Stress> list = stressRepository.findLastWeekData(member, lastStart, type);
        return list;
    }

    @Transactional
    public Long getCountLastWeekList(Member member, LocalDateTime lastStart, StressType type) {
        Long count = stressRepository.countLastWeekData(member, lastStart, type);
        return count;
    }

    @Transactional
    public Long getCountLastWeekDiseaseList(Member member, LocalDateTime lastStart) {
        Long count = diseaseRepository.countLastWeekData(member, lastStart);
        return count;
    }


    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }



}
