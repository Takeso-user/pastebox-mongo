package pl.malcew.testbitly.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.malcew.testbitly.entity.PasteboxEntity;
import pl.malcew.testbitly.entity.PasteboxRecord;
import pl.malcew.testbitly.entity.PostboxScope;
import pl.malcew.testbitly.repo.PasteboxRepo;

import java.util.*;

@Service
@Log4j2
public class PasteboxService {

    private final PasteboxRepo pasteboxRepo;
    private final Map<String, Long> expirationTime = Map.of(
            "10m", 600000L,
            "1h", 3600000L,
            "3h", 10800000L,
            "1d", 86400000L,
            "1w", 604800000L,
            "1m", 2592000000L,
            "unlimited", 0L
    );

    public Map<String, Long> getExpirationTimeMap() {
        return expirationTime;
    }

    public PasteboxService(PasteboxRepo pasteboxRepo) {
        this.pasteboxRepo = pasteboxRepo;
    }

    public List<PasteboxRecord> getLastTenPasteboxes() {
        log.info("Getting the last ten pasteboxes");
        return pasteboxRepo.findAll().stream()
                .filter(p -> (expirationTime.get(p.getExpirationTime()) + p.getCreationTime() > System.currentTimeMillis()
                        || p.getExpirationTime().equals("unlimited"))
                        && p.getScope().equals(PostboxScope.PUBLIC))
                .sorted(Comparator.comparing(PasteboxEntity::getCreationTime).reversed())
                .limit(10)
                .map(PasteboxEntity::toRecord)
                .toList();
    }

    public String savePastebox(PasteboxEntity pasteboxEntity) {
        log.info("Saving a new pastebox: {}", pasteboxEntity);
        PasteboxEntity res;
        try {
            pasteboxEntity.setId(UUID.randomUUID().toString());
            pasteboxEntity.setCreationTime(System.currentTimeMillis());
            res =  pasteboxRepo.save(pasteboxEntity);
        } catch (Exception e) {
            log.error("Failed to save pastebox: {}", pasteboxEntity, e);
            throw new RuntimeException(e);
        }
        log.info("Successfully saved pastebox with id: {}", res.getId());
        return res.getId();
    }


    public String getById(String uuid) {
        log.info("Getting the paste with id: {}", uuid);
        return Objects
                .requireNonNull(pasteboxRepo
                        .findById(uuid)
                        .orElse(null))
                .getContent();
    }

    public String deleteById(String uuid) {
        log.info("Deleting the paste with id: {}", uuid);
        pasteboxRepo.deleteById(uuid);
        return String.format("the paste with id %s has been successfully deleted", uuid);
    }
}
