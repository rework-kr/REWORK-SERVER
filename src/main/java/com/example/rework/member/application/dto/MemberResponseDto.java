package com.example.rework.member.application.dto;



import com.example.rework.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
