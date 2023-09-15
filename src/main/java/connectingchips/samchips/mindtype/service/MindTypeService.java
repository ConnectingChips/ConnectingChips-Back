package connectingchips.samchips.mindtype.service;

import connectingchips.samchips.mindtype.dto.CreateMindTypeInput;
import connectingchips.samchips.mindtype.dto.MindTypeOutput;
import connectingchips.samchips.mindtype.entity.MindType;
import connectingchips.samchips.mindtype.repository.MindTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MindTypeService {
    private final MindTypeRepository mindTypeRepository;
    public void createMindType(CreateMindTypeInput createMindTypeInput) {
        mindTypeRepository.save(MindType.builder()
                .name(createMindTypeInput.getName())
                .build());
    }

    public MindTypeOutput findMindType(Long mindTypeId) {
        MindType verifiedMindType = findVerifiedMindType(mindTypeId);
        return MindTypeOutput.builder()
                .mindTypeId(verifiedMindType.getMindTypeId())
                .name(verifiedMindType.getName())
                .minds(verifiedMindType.getMinds())
                .build();
    }

    private MindType findVerifiedMindType(Long mindTypeId) {
        Optional<MindType> byId = mindTypeRepository.findById(mindTypeId);
        return byId.orElseThrow(() ->
                new RuntimeException("존재하지 않는 작심 종류 번호입니다."));
    }

    public void deleteMindType(Long mindTypeId) {
        mindTypeRepository.delete(findVerifiedMindType(mindTypeId));
    }

}
