package org.alphatrack.screensociety.services.contracts;

import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.models.Tag;



import java.util.Set;

public interface TagService {

    Set<Tag> getAll();

    Tag createTag(TagRequestDto tagRequestDto);

    void deleteTag(Long id);

    Tag editTag(Long id, TagRequestDto tagRequestDto);

    Tag getByName(String tagName);

    Tag resolveOrCreate(String tagName);

    Set<Tag> resolveOrCreate(Set<String> tagNames);
}
