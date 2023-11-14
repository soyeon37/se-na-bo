package com.senabo.domain.emergency.service;

import com.senabo.common.message.ParsingMessageService;
import com.senabo.config.firebase.FCMService;
import com.senabo.domain.emergency.dto.response.EmergencyResponse;
import com.senabo.domain.emergency.entity.Emergency;
import com.senabo.domain.emergency.entity.EmergencyType;
import com.senabo.domain.emergency.repository.EmergencyRepository;
import com.senabo.domain.member.entity.Member;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.DataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmergencyService {
    private final FCMService fcmService;
    private final EmergencyRepository emergencyRepository;
    private final ParsingMessageService parsingMessageService;


    public void ramdomEmergency(Member member) {
        if (!ramdomSend()) return;
        EmergencyType type;
        do {
            type = comb();
        } while (checkDuplicate(member, type));

        String body = "";
        String dogName = parsingMessageService.parseLastCharacter(member.getDogName());
        switch (type) {
            case POOP -> body = "집에서 냄새가 나요";
            case STOMACHACHE -> body = dogName + "가 아픈 것 같아요";
            case ANXIETY -> body = "외부 소음으로 인해 불안함을 느낍니다";
            case DEPRESSION -> body = dogName + "가 무기력함을 느낍니다";
            case CRUSH -> body = dogName + "가 잠을 자지 않아요";
            case BITE -> body = "물림 사고가 발생했어요!";
            case WALK -> body = dogName + "가 산책을 가고 싶어해요";
            case BARKING -> body = dogName + "가 짖어서 민원이 들어올 수 있습니다";
            case VOMITING -> body = dogName + "의 상태가 좋지 않습니다";
        }
        fcmService.sendNotificationByToken("세상에 나쁜 보호자는 있다", body, member.getDeviceToken());
        saveEmergency(member, type);
    }

    public boolean checkDuplicate(Member member, EmergencyType type) {
        Optional<Emergency> emergencyOptional = emergencyRepository.findLatestEmergency(member);
        // 최근 7일 내 데이터 확인, 7일 내 알림이 없었으면 false
        if (emergencyOptional.isEmpty()) return false;
        Emergency emergency = emergencyOptional.get();
        EmergencyType lastType = emergency.getType();
        return lastType == type;
    }

    public boolean ramdomSend() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        // 70%의 확률로 false, 30%의 확률로 true
        return randomValue < 0.3;
    }

    public EmergencyType comb() {
        EmergencyType[] types = EmergencyType.values();
        int randomIndex = new Random().nextInt(types.length);
        return types[randomIndex];
    }

    @Transactional
    public void saveEmergency(Member member, EmergencyType type) {
        emergencyRepository.save(new Emergency(member, type, false));
        try {
            emergencyRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataException(ExceptionMessage.FAIL_SAVE_DATA);
        }
    }

    @Transactional
    public EmergencyResponse solvedEmergency(Member member, Long id) {
       Optional<Emergency> emergencyOptional = emergencyRepository.findByIdAndMemberId(id, member);
       if(emergencyOptional.isEmpty()) throw new DataException(ExceptionMessage.DATA_NOT_FOUND);
       Emergency emergency = emergencyOptional.get();
       emergency.update(true);
       return EmergencyResponse.from(emergency);
    }

    // 7일간 발생한 돌방상황 조회
    public List<Emergency> getEmergencyLastWeek(Member member) {
        return emergencyRepository.findLastWeekEmergency(member);
    }
}
