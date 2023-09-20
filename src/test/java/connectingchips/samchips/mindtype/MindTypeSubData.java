package connectingchips.samchips.mindtype;

import connectingchips.samchips.mindtype.dto.CreateMindTypeRequest;
import connectingchips.samchips.mindtype.dto.MindTypeResponse;
import connectingchips.samchips.mindtype.entity.MindType;

public class MindTypeSubData {


    public static final String MIND_TYPE_NAME = "작심 종류 이름";

    public CreateMindTypeRequest getCreateMindTypeRequest(){
        CreateMindTypeRequest mindTypeRequest = new CreateMindTypeRequest();
        mindTypeRequest.setName(MIND_TYPE_NAME);
        return mindTypeRequest;
    }
    public MindType getMindType(){
        MindType mindType = new MindType();
        mindType.setName(MIND_TYPE_NAME);
        return mindType;
    }
    public MindTypeResponse getMindTypeResponse(){
        return MindTypeResponse.of(getMindType());
    }
}
