package pl.malcew.testbitly.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.malcew.testbitly.entity.PasteboxEntity;
@Repository
public interface PasteboxRepo extends MongoRepository<PasteboxEntity, String>{

}
