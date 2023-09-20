package connectingchips.samchips.mindtype.service;

import connectingchips.samchips.mindtype.MindTypeSubData;
import connectingchips.samchips.mindtype.dto.MindTypeResponse;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.mindtype.repository.MindTypeRepository;
import connectingchips.samchips.utils.CustomBeanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MindTypeServiceTest {

    @Mock
    private MindTypeRepository mindTypeRepository;
    @Mock
    private CustomBeanUtils<MindType> beanUtils;
    private MindTypeSubData mindTypeSubData;
    @InjectMocks
    private MindTypeService mindTypeService;
    @BeforeEach
    void init() {
        mindTypeSubData = new MindTypeSubData();
    }
    @DisplayName("작심 종류 생성")
    @Test
    void createMindType() {
        //given
        MindType mindType = mindTypeSubData.getMindType();
        given(mindTypeRepository.save(Mockito.any(MindType.class))).willReturn(mindType);
        //when
        MindTypeResponse result = mindTypeService.createMindType(mindTypeSubData.getCreateMindTypeRequest());
        //then
        assertThat(result.getName()).isEqualTo(mindType.getName());
    }

//    @DisplayName("작심 종류 탐색")
//    @Test
//    void findMindType() {
//        MindType mindType = mindTypeSubData.getMindType();
//        mindType.
//    }

    @Test
    void deleteMindType() {
    }

    @Test
    void updateMindType() {
    }
}