package org.alphatrack.screensociety.controllers.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.dto.response.TagResponseDto;
import org.alphatrack.screensociety.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    //TODO inject the dependencies


    public TagRestController() {
    }

    @Operation(summary = "Retrieves all exsiting tags")
    @GetMapping
    public List<TagResponseDto> getAll() {
        return null;//service.getAll();
    }

    @Operation(summary = "Creates a tag if it doesnt exist")
    @PostMapping
    public TagResponseDto createTag(@Valid @RequestBody TagRequestDto tagDTO) {
        return null; // service.create(tagDTO);
    }

    @Operation(summary = "Deletes a tag, ADMIN only")
    @DeleteMapping("/{targetId}")
    public void deleteTag(@PathVariable int targetId, @AuthenticationPrincipal User currentUser) {
        //service.delete(targetId,currentUser);
    }

    @Operation(summary = "Edits a tag, ADMIN only")
    @PutMapping("/{targetId}")
    public TagResponseDto editTag(@PathVariable int targetId, @AuthenticationPrincipal User currentUser,
                                  @RequestBody TagRequestDto tagDTO) {
        return null;//service.edit(targetId,currentUser,tagDTO);
    }
}
