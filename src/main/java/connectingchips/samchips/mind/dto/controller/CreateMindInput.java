package connectingchips.samchips.mind.dto.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMindInput {

    private String name;
    private String introduce;
    private String writeFormat;
    private String backgroundImage;
    private String exampleImage;


}
