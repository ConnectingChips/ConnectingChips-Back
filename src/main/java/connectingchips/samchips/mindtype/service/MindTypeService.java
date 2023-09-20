package connectingchips.samchips.mindtype.service;

import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.mindtype.dto.CreateMindTypeRequest;
import connectingchips.samchips.mindtype.dto.MindTypeResponse;
import connectingchips.samchips.mindtype.dto.UpdateMindTypeRequest;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.mindtype.repository.MindTypeRepository;
import connectingchips.samchips.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static connectingchips.samchips.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional

public class MindTypeService {
    private final MindTypeRepository mindTypeRepository;
    private final CustomBeanUtils<MindType> beanUtils;
    public MindTypeResponse createMindType(CreateMindTypeRequest createMindTypeRequest) {
        return MindTypeResponse.of(mindTypeRepository.save(MindType.builder()
                .name(createMindTypeRequest.getName())
                .build()));
    }

    public MindTypeResponse findMindType(Long mindTypeId) {
        MindType verifiedMindType = findVerifiedMindType(mindTypeId);
        return MindTypeResponse.of(verifiedMindType);
    }

    private MindType findVerifiedMindType(Long mindTypeId) {
        Optional<MindType> byId = mindTypeRepository.findById(mindTypeId);
        return byId.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_MIND_TYPE_ID));
    }

    public void deleteMindType(Long mindTypeId) {
        mindTypeRepository.delete(findVerifiedMindType(mindTypeId));
    }

    public MindTypeResponse updateMindType(Long mindTypeId, UpdateMindTypeRequest updateMindTypeRequest) {
        MindType verifiedMindType = findVerifiedMindType(mindTypeId);
        return MindTypeResponse.of(beanUtils.copyNonNullProperties(MindType
                .builder()
                .name(updateMindTypeRequest.getName())
                .build(), verifiedMindType));
    }

}
