package org.alphatrack.screensociety.controllers.rest;


import jakarta.validation.Valid;
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

    @GetMapping
    public List<Tag> getAll(){
        service.getAll();
    }

    @PostMapping
    public void createTag (@Valid @RequestBody TagDTO tagDTO){
        service.create(tagDTO);
    }

    @DeleteMapping("/{targetId}")
    public void deleteTag(@PathVariable int targetId, @AuthenticationPrincipal User currentUser){
        service.delete(targetId,currentUser);
    }

    @PutMapping("/{targetId}")
    public void editTag(@PathVariable int targetId, @AuthenticationPrincipal User currentUser, @RequestBody TagDTO tagDTO){
        service.edit(targetId,currentUser,tagDTO);
    }
}
