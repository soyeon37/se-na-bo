package com.senabo.domain.member.service;

import com.senabo.config.security.jwt.TokenInfo;
import com.senabo.config.security.jwt.TokenProvider;
import com.senabo.domain.member.dto.request.FirebaseAuthRequest;
import com.senabo.domain.member.dto.request.SignOutRequest;
import com.senabo.domain.member.dto.request.SignUpRequest;
import com.senabo.domain.member.dto.request.UpdateInfoRequest;
import com.senabo.domain.member.dto.response.FirebaseAuthResponse;
import com.senabo.domain.member.dto.response.MemberResponse;
import com.senabo.domain.member.dto.response.ReIssueResponse;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Report;
import com.senabo.domain.member.entity.Role;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.member.repository.ReportRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.TokenCheckFailException;
import com.senabo.exception.model.TokenNotFoundException;
import com.senabo.exception.model.UserAuthException;
import com.senabo.exception.model.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;


    public boolean checkEmail(String email) {
        boolean duplicateYn = false;
        if (memberRepository.existsByEmail(email)) {
            duplicateYn = true;
        }
        return duplicateYn;
    }

    public MemberResponse signUp(SignUpRequest request) {
        Member member = memberRepository.save(
                new Member(request.name(), request.email(), request.species(), request.sex(), request.houseLatitude(), request.houseLogitude(), request.uid(), request.deviceToken()));

        Report report = reportRepository.save(
                new Report(member, 1, 0, 50)
        );
        try {
            memberRepository.flush();
            reportRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, roles);

        TokenInfo tokenInfo = tokenProvider.generateToken(authentication);

        refreshTokenService.setValues(tokenInfo.getRefreshToken(), member.getEmail());

        return MemberResponse.from(member);
    }

    @Transactional
    public void removeMember(String email, SignOutRequest request) {
        try {
            refreshTokenService.delValues(request.refreshToken());
            memberRepository.deleteByEmail(email);
        } catch (DataIntegrityViolationException e) {
            throw new UserAuthException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public MemberResponse getInfo(String email) {
        Member member = findByEmail(email);
        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateInfo(String email, UpdateInfoRequest request) {
        Member member = findByEmail(email);
        member.update(request);
        return MemberResponse.from(member);
    }


    @Transactional
    public FirebaseAuthResponse signIn(FirebaseAuthRequest request) {
//        try {
//            firebaseAuth.verifyIdToken(request.idToken());
//        } catch (FirebaseAuthException e) {
//            return new FirebaseAuthResponse();
//        }

        Optional<Member> member = memberRepository.findByEmail(request.email());
        if (member.isEmpty()) {
            return new FirebaseAuthResponse(false);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.email(), null, roles);

        TokenInfo tokenInfo = tokenProvider.generateToken(authentication);

        member.get().setUid(request.uid());
        member.get().setDeviceToken(request.deviceToken());

        refreshTokenService.setValues(tokenInfo.getRefreshToken(), request.email());

        return new FirebaseAuthResponse(true,
                FirebaseAuthResponse.SignInResponse.builder()
                        .id(member.get().getId())
                        .name(member.get().getName())
                        .email(member.get().getEmail())
                        .species(member.get().getSpecies())
                        .sex(member.get().getSex())
                        .houseLatitude(member.get().getHouseLatitude())
                        .houseLogitude(member.get().getHouseLogitude())
                        .uid(member.get().getUid())
                        .deviceToken(member.get().getDeviceToken())
                        .affection(member.get().getAffection())
                        .stressLevel(member.get().getStressLevel())
                        .totalTime(member.get().getTotalTime())
                        .exitTime(member.get().getExitTime())
                        .enterTime(member.get().getEnterTime())
                        .createTime(member.get().getCreateTime())
                        .updateTime(member.get().getUpdateTime())
                        .token(tokenInfo)
                        .build()
        );
    }

    @Transactional
    public void signOut(SignOutRequest request, String email) {
        Member member = findByEmail(email);
        member.setUid(null);
        member.setDeviceToken(null);
        refreshTokenService.delValues(request.refreshToken());
    }

    @Transactional
    public ReIssueResponse reissue(String refreshToken, Authentication authentication) {
        if (authentication.getName() == null) {
            throw new UserAuthException(ExceptionMessage.NOT_AUTHORIZED_ACCESS);
        }

        if (!tokenProvider.validateToken(refreshToken)) {
            Member member = findByEmail(authentication.getName());
            member.setUid(null);
            member.setDeviceToken(null);
            refreshTokenService.delValues(refreshToken);
            throw new TokenNotFoundException(ExceptionMessage.TOKEN_VALID_TIME_EXPIRED);
        }

        String email = refreshTokenService.getValues(refreshToken);
        if (email == null || !email.equals(authentication.getName())) {
            throw new TokenCheckFailException(ExceptionMessage.MISMATCH_TOKEN);
        }

        return createAccessToken(refreshToken, authentication);
    }

    private ReIssueResponse createAccessToken(String refreshToken, Authentication authentication) {
        if (tokenProvider.checkExpiredToken(refreshToken)) {
            TokenInfo tokenInfo = tokenProvider.generateAccessToken(authentication);
            return ReIssueResponse.from(tokenInfo.getAccessToken(), "SUCCESS");
        }

        return ReIssueResponse.from(tokenProvider.generateAccessToken(authentication).getAccessToken(), "GENERAL_FAILURE");
    }

    @Transactional
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
