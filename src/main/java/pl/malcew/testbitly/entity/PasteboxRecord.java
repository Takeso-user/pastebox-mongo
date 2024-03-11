package pl.malcew.testbitly.entity;

public record PasteboxRecord(
        String id,
        String expirationTime,
        PostboxScope scope,
        String content
) {
}
