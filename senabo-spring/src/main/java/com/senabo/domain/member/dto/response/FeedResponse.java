package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Feed;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FeedResponse(
        Long id,
        Long memberId,
        LocalDateTime dateTime
) {
    public static FeedResponse from(Feed feed){
        return FeedResponse.builder()
                .id(feed.getId())
                .memberId(feed.getMemberId().getId())
                .dateTime(feed.getUpdateTime())
                .build();
    }
}
