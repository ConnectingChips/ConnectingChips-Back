package connectingchips.samchips.mind.service;

import connectingchips.samchips.mind.dto.service.FindMindOutput;
import connectingchips.samchips.mind.entity.Mind;

import java.util.List;

public interface MindService {

    FindMindOutput findMind(long mindId);
    List<FindMindOutput> findMinds();

}
