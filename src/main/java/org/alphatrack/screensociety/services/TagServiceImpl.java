package org.alphatrack.screensociety.services;

import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.repositories.contracts.TagRepository;
import org.alphatrack.screensociety.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;


@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public Set<Tag> getAll() {
        return new HashSet<>(tagRepository.findAll());
    }

    @Override
    public Tag createTag(TagRequestDto tagRequestDto) {

        String formattedTagName = tagRequestDto.getName().toLowerCase().trim();

        if (tagRepository.findByName(formattedTagName).isPresent()) {
            throw new DuplicateRequestException("Tag with such name already exist");
        } else {
            return tagRepository.save(Tag.builder()
                    .name(formattedTagName)
                    .build());
        }

    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag editTag(Long id, TagRequestDto tagRequestDto) {

        String formattedTagName = tagRequestDto.getName().toLowerCase().trim();

        Tag tagToUpdate = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        tagRepository.findByName(formattedTagName).ifPresent(existingTag -> {
            if (!existingTag.getId().equals(id)) {
                throw new EntityExistsException("Tag name already exists");
            }
        });

        tagToUpdate.setName(formattedTagName);

        return tagRepository.save(tagToUpdate);
    }

    @Override
    public Tag getByName(String tagName) {
        return tagRepository.findByName(tagName).orElseThrow(() -> new EntityNotFoundException("Tag not found"));
    }
}
