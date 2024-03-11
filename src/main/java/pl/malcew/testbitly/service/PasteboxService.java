package pl.malcew.testbitly.service;

import org.springframework.stereotype.Service;
import pl.malcew.testbitly.entity.PasteboxEntity;
import pl.malcew.testbitly.entity.PasteboxRecord;
import pl.malcew.testbitly.entity.PostboxScope;
import pl.malcew.testbitly.repo.PasteboxRepo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
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
        List<PasteboxEntity> lastTen = pasteboxRepo.findAll();
        lastTen.forEach(System.out::println);
        lastTen = lastTen
                .stream()
                .filter(p ->
                        expirationTime.get(p.getExpirationTime()) + p.getCreationTime() > System.currentTimeMillis()
                                || p.getExpirationTime().equals("unlimited")
                )
                .peek(p ->
                        {
                            System.out.println("expirationTime: " + expirationTime);
                            System.out.println("expirationTime.get(p.getExpirationTime()): " + expirationTime.get(p.getExpirationTime()));
                            System.out.println("p.: " + p);
                            System.out.println("p.getExpirationTime(): " + p.getExpirationTime());
                            System.out.println("creation time: " + p.getCreationTime());
                            System.out.println("current time: " + System.currentTimeMillis());
                        }
                )
                .toList();


        if (lastTen.isEmpty()) return List.of();
        lastTen = lastTen
                .stream()
                .filter(publicPost -> publicPost.getScope().equals(PostboxScope.PUBLIC))

                .toList();
        if (lastTen.size() > 10) {
            var res=  lastTen.subList(lastTen.size() - 10, lastTen.size());
            return res.stream().map(PasteboxEntity::toRecord).toList();
        }
        return lastTen.stream().map(PasteboxEntity::toRecord).toList();
    }

    public String savePastebox(PasteboxEntity pasteboxEntity) {
        PasteboxEntity res;
        try {
            pasteboxEntity.setId(UUID.randomUUID().toString());
            pasteboxEntity.setCreationTime(System.currentTimeMillis());
            System.out.println("!service pasteboxEntity: " + pasteboxEntity);
            res =  pasteboxRepo.save(pasteboxEntity);
        } catch (Exception e) {
            System.out.println("!service pasteboxEntity: " + pasteboxEntity + " !!!\n" + e);
            throw new RuntimeException(e);
        }
        return res.getId();
    }


    public String getById(String id) {
        return Objects.requireNonNull(pasteboxRepo.findById(id).orElse(null)).getContent();
    }

    public void deleteById(String uuid) {
        pasteboxRepo.deleteById(uuid);
    }
}
