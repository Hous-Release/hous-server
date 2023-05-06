package hous.api.service.room.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import hous.common.constant.Constraint;
import io.swagger.annotations.ApiModelProperty;
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
public class SetRoomNameRequestDto {

	@ApiModelProperty(value = "방 이름", example = "러블리더블리")
	@NotBlank(message = "{room.name.notBlank}")
	@Size(max = Constraint.ROOM_NAME_MAX, message = "{room.name.max}")
	private String name;

	public static SetRoomNameRequestDto of(String name) {
		return SetRoomNameRequestDto.builder()
			.name(name)
			.build();
	}
}
