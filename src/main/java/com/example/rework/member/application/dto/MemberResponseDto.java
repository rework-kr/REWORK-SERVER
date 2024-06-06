package com.example.rework.member.application.dto;



import com.example.rework.auth.MemberRole;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.NonMemberEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class MemberResponseDto {
	@Getter
	@AllArgsConstructor
	@Builder
	@NoArgsConstructor
	public static class MemberLoginResponseDto {

		String accessToken;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	@NoArgsConstructor
	public static class MemberInfoResponseDto {

		String email;
		boolean initialPasswordUpdateState;
		MemberRole memberRole;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	@NoArgsConstructor
	public static class MemberCreateResponseDto {

		Long memberId;
		String name;
		String userId;
		String memberRole;
		public MemberCreateResponseDto(Member member) {
			this.name = member.getName();
        	this.userId = member.getUserId();
        	this.memberRole = String.valueOf(member.getRole());
		}

	}

	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class NonMemberEmailListResponseDto {

		@NotEmpty(message = "아이디 설정은 필수입니다.")
		@Size(min = 4, max = 32, message = "아이디를 4~32글자로 설정해주세요.")
		private String email;

		private Long NonMemberEmailId;

		private LocalDateTime createdAt;

		public NonMemberEmailListResponseDto(NonMemberEmail nonMemberEmail) {
			this.email = nonMemberEmail.getEmail();
			this.NonMemberEmailId = nonMemberEmail.getId();
			this.createdAt = nonMemberEmail.getCreatedAt();
		}
	}


}
