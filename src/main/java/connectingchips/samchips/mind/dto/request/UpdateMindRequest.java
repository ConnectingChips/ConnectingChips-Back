package connectingchips.samchips.mind.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMindRequest {

    private  String name;
    private  String introduce;
    private  String writeFormat;
    private  String introImage;
    private  String pageImage;
    private  String totalListImage;
    private  String myListImage;


}
