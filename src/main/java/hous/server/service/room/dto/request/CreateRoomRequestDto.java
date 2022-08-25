package hous.server.service.room.dto.request;

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
public class CreateRoomRequestDto {

    @ApiModelProperty(value = "방 이름", example = "러블리더블리")
    @NotBlank(message = "{room.name.notBlank}")
    @Size(max = 8, message = "{room.name.max}")
    private String name;
}
