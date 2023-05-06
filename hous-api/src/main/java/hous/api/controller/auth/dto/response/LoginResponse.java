package hous.api.controller.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import hous.api.service.auth.dto.response.TokenResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class LoginResponse {

	private Long userId;
	private TokenResponse token;
	private boolean isJoiningRoom;

	@JsonProperty("isJoiningRoom")
	public boolean isJoiningRoom() {
		return isJoiningRoom;
	}

	public static LoginResponse of(Long userId, TokenResponse token, boolean isJoiningRoom) {
		return LoginResponse.builder()
			.userId(userId)
			.token(token)
			.isJoiningRoom(isJoiningRoom)
			.build();
	}
}
