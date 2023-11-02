package com.senabo.domain.member.dto.request;

public record FirebaseAuthRequest(String idToken,
                                  String uid,
                                  String email,
                                  String deviceToken) {
}
