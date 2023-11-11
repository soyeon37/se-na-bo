package com.senabo.common.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParsingMessageService {

    public String parseLastCharacter(String input) {
        // 문자열이 비어있는 경우 예외처리
        if (input.isEmpty()) {
            log.info("입력된 문자열이 비어있습니다.");
            return "";
        }
        String dogName = input;
        // 문자열의 마지막 문자 추출
        char lastChar = input.charAt(input.length() - 1);

        // 추출한 문자가 한글인지 확인
        if (isKoreanCharacter(lastChar)) {
            // 한글 자음과 모음 범위 확인
            if (isJaeum(lastChar)) {
                log.info("맨 끝 자리는 한글 자음입니다.");
                dogName += "이";
            } else if (isMoeum(lastChar)) {
                log.info("맨 끝 자리는 한글 모음입니다.");
            } else {
                log.info("맨 끝 자리는 한글 자음도 모음도 아닙니다.");
            }
        } else {
            log.info("맨 끝 자리는 한글이 아닙니다.");
        }

        return dogName;
    }

    // 문자가 한글인지 확인하는 메서드
    private static boolean isKoreanCharacter(char ch) {
        return Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.HANGUL_SYLLABLES;
    }

    // 문자가 한글 자음인지 확인하는 메서드
    private static boolean isJaeum(char ch) {
        return ch >= 'ㄱ' && ch <= 'ㅎ';
    }

    // 문자가 한글 모음인지 확인하는 메서드
    private static boolean isMoeum(char ch) {
        return ch >= 'ㅏ' && ch <= 'ㅣ';
    }
}
