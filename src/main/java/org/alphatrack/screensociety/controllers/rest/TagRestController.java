package org.alphatrack.screensociety.controllers.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.dto.response.TagResponseDto;
import org.alphatrack.screensociety.models.User;
import org.alphatrack.screensociety.services.contracts.TagService;
import org.alphatrack.screensociety.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagService tagService;
    private final ModelMapper modelMapper;

    @Autowired
    public TagRestController(TagService tagService,ModelMapper modelMapper) {
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Retrieves all existing tags")
    @GetMapping
    public List<TagResponseDto> getAll() {

        return modelMapper.tagsToTagsDto(tagService.getAll());

    }

    @GetMapping("/{name}")
    public TagResponseDto getByName(@PathVariable String name){
        return modelMapper.tagToTagDto(tagService.getByName(name));
    }

    @Operation(summary = "Creates a tag if it doesnt exist")
    @PostMapping
    public TagResponseDto createTag(@Valid @RequestBody TagRequestDto tagDTO) {

        return modelMapper.tagToTagDto(tagService.createTag(tagDTO));

    }

    @Operation(summary = "Deletes a tag, ADMIN only")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{targetId}")
    public void deleteTag(@PathVariable Long targetId, @AuthenticationPrincipal User currentUser) {

        tagService.deleteTag(targetId);

    }

    @Operation(summary = "Edits a tag, ADMIN only")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{targetId}")
    public TagResponseDto editTag(@PathVariable Long targetId, @RequestBody TagRequestDto tagDTO) {

        return modelMapper.tagToTagDto(tagService.editTag(targetId,tagDTO));

    }
}
