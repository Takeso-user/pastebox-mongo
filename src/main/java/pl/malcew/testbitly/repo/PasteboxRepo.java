package pl.malcew.testbitly;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.malcew.testbitly.entity.PasteboxEntity;

public interface PasteboxRepo extends MongoRepository<PasteboxEntity, String>{
}
