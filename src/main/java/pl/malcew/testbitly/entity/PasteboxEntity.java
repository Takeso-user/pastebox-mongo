package pl.malcew.testbitly.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pastebox")
@Data
@Builder
@ToString
public class PasteboxEntity {

    @Id
    private String id;
    private String expirationTime;
    private Long creationTime;
    private PostboxScope scope;
    private String content;

    public PasteboxRecord toRecord() {
        return new PasteboxRecord(id, expirationTime, scope, content);
    }
}
