package pl.malcew.testbitly.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.malcew.testbitly.entity.PasteboxEntity;
import pl.malcew.testbitly.entity.PasteboxRecord;
import pl.malcew.testbitly.exception.WrongTimeFormatException;
import pl.malcew.testbitly.service.PasteboxService;

import java.util.List;

@RestController
@RequestMapping("api/v1/paste")
@Log4j2
public class MainController {
    private final PasteboxService pasteboxService;

    public MainController(PasteboxService pasteboxService) {
        this.pasteboxService = pasteboxService;

    }

    @GetMapping("/get-ten")
    public List<PasteboxRecord> getLastTenPasteboxes() {
        var lastTen = pasteboxService.getLastTenPasteboxes();
        log.info("last ten in controller: " + lastTen);
        return lastTen;
    }

    @GetMapping("/get/{uuid}")
    public String getById(@PathVariable String uuid) {
        log.info("uuid of the record is: " + uuid);
        return pasteboxService.getById(uuid);
    }

    @PostMapping("/create")
    public String createNewPastebox(@RequestBody PasteboxEntity pasteboxEntity) {

        if (pasteboxEntity.getExpirationTime() == null) {
            pasteboxEntity.setExpirationTime("unlimited");
        }
        if (!pasteboxService.getExpirationTimeMap().containsKey(pasteboxEntity.getExpirationTime())) {
            log.error("Wrong time format!");
            throw new WrongTimeFormatException("""
                    Wrong time format.\s
                    The expiration time should be as one of the following:\s
                    10m, 1h, 3h, 1d, 1w, 1m, unlimited""");
        }
        log.info("Controller pasteboxEntity: " + pasteboxEntity);
        String uuid = pasteboxService.savePastebox(pasteboxEntity);
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/paste/get/{uuid}")
                .buildAndExpand(uuid)
                .toUriString();
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteById(@PathVariable String uuid) {
        log.info("trying to delete the record with uuid: " + uuid);
        return pasteboxService.deleteById(uuid);
    }
}
