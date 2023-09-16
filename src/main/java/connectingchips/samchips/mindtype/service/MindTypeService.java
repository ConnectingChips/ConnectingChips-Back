package connectingchips.samchips.mindtype.service;

import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.exception.ExceptionCode;
import connectingchips.samchips.mindtype.dto.CreateMindTypeRequest;
import connectingchips.samchips.mindtype.dto.MindTypeResponse;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.mindtype.repository.MindTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static connectingchips.samchips.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class MindTypeService {
    private final MindTypeRepository mindTypeRepository;
    public void createMindType(CreateMindTypeRequest createMindTypeRequest) {
        mindTypeRepository.save(MindType.builder()
                .name(createMindTypeRequest.getName())
                .build());
    }

    public MindTypeResponse findMindType(Long mindTypeId) {
        MindType verifiedMindType = findVerifiedMindType(mindTypeId);
        return MindTypeResponse.builder()
                .mindTypeId(verifiedMindType.getMindTypeId())
                .name(verifiedMindType.getName())
                .minds(verifiedMindType.getMinds())
                .build();
    }

    private MindType findVerifiedMindType(Long mindTypeId) {
        Optional<MindType> byId = mindTypeRepository.findById(mindTypeId);
        return byId.orElseThrow(() ->
                new BadRequestException(NOT_FOUND_MIND_TYPE_ID));
    }

    public void deleteMindType(Long mindTypeId) {
        mindTypeRepository.delete(findVerifiedMindType(mindTypeId));
    }

}
