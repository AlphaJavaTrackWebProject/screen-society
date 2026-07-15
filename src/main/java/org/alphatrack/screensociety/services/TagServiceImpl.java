package org.alphatrack.screensociety.services;

import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.exceptions.DuplicateEntityException;
import org.alphatrack.screensociety.exceptions.EntityNotFoundException;
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
            throw new DuplicateEntityException("Tag","name",formattedTagName);
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

        Tag tagToUpdate = tagRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Tag","Id",String.valueOf(id)));

        tagToUpdate.setName(formattedTagName);

        return tagRepository.save(tagToUpdate);
    }

    @Override
    public Tag getByName(String tagName) {
        return tagRepository.findByName(tagName).orElseThrow(() ->
                new EntityNotFoundException("Tag","name",tagName));
    }
}
