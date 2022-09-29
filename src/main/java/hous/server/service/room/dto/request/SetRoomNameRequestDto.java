package hous.server.service.room.dto.request;

import hous.server.domain.common.Constraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetRoomNameRequestDto {

    @ApiModelProperty(value = "방 이름", example = "러블리더블리")
    @NotBlank(message = "{room.name.notBlank}")
    @Size(max = Constraint.ROOM_NAME_MAX, message = "{room.name.max}")
    private String name;
}
