package org.alphatrack.screensociety.services.contracts;

import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.models.User;

import java.util.Set;

public interface TagService {

    Set<Tag> getAll();

    Tag createTag(TagRequestDto tagRequestDto);

    void deleteTag(Long id);

    Tag editTag(Long id, TagRequestDto tagRequestDto);

    Tag getByName(String tagName);
}
