package pl.malcew.testbitly.controller;

import org.springframework.web.bind.annotation.*;
import pl.malcew.testbitly.entity.PasteboxEntity;
import pl.malcew.testbitly.entity.PasteboxRecord;
import pl.malcew.testbitly.exception.WrongTimeFormatException;
import pl.malcew.testbitly.service.PasteboxService;

import java.util.List;

@RestController
@RequestMapping("api/v1/paste")
public class MainController {
    private final PasteboxService pasteboxService;

    public MainController(PasteboxService pasteboxService) {
        this.pasteboxService = pasteboxService;
    }

    @GetMapping("/get-ten")
    public List<PasteboxRecord> getLastTenPasteboxes() {
        System.out.println("last ten in controller: ");
        return pasteboxService.getLastTenPasteboxes();
    }

    @GetMapping("/get/{uuid}")
    public PasteboxEntity getById(String uuid) {
        return pasteboxService.getById(uuid);
    }

    @PostMapping("/create")
    public void createNewPastebox(@RequestBody PasteboxEntity pasteboxEntity) {

        if (pasteboxEntity.getExpirationTime() == null) {
            pasteboxEntity.setExpirationTime("unlimited");
        }
        if (!pasteboxService.getExpirationTimeMap().containsKey(pasteboxEntity.getExpirationTime())) {
            throw new WrongTimeFormatException("""
                    Wrong time format.\s
                    The expiration time should be as one of the following:\s
                    10m, 1h, 3h, 1d, 1w, 1m, unlimited""");
        }
        System.out.println("!controller pasteboxEntity: " + pasteboxEntity);
        pasteboxService.savePastebox(pasteboxEntity);
    }

    @DeleteMapping("/delete/{uuid}")
    public void deleteById(@PathVariable String uuid){
        pasteboxService.deleteById(uuid);
    }
}
