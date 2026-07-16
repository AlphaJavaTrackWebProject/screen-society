package org.alphatrack.screensociety.services;

import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.alphatrack.screensociety.dto.request.TagRequestDto;
import org.alphatrack.screensociety.models.Tag;
import org.alphatrack.screensociety.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;
import java.util.Set;


@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    TagRepository mockRepository;

    @InjectMocks
    TagServiceImpl mockService;

    private TagRequestDto mockTagDto;
    private Tag mockTag;

    @BeforeEach
    public void setUp() {
        mockTagDto = new TagRequestDto("mocktag");
        mockTag = Mockito.mock(Tag.class);
    }

    @Test
    public void createTag_Should_Throw_WhenTagExists() {
        Mockito.when(mockRepository.findByName(mockTagDto.getName()))
                .thenReturn(Optional.of(new Tag()));

        Assertions.assertThrows(DuplicateRequestException.class, () -> mockService.createTag(mockTagDto));
    }

    @Test
    public void createTag_Should_Create_New_Tag_IfItDoesntExist() {
        Mockito.when(mockRepository.findByName(mockTagDto.getName()))
                .thenReturn(Optional.empty());

        mockService.createTag(mockTagDto);

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(Mockito.any(Tag.class));
    }

    @Test
    public void deleteTag_Should_Delete_If_TagIdIsValid() {
        mockService.deleteTag(1L);

        Mockito.verify(mockRepository, Mockito.times(1))
                .deleteById(1L);
    }

    @Test
    public void getByName_Should_Return_TagIfFound() {

        Tag realTag = new Tag();
        realTag.setName(mockTagDto.getName());

        Mockito.when(mockRepository.findByName(mockTagDto.getName()))
                .thenReturn(Optional.of(realTag));

        Tag result = mockService.getByName(mockTagDto.getName());

        Assertions.assertEquals(mockTagDto.getName(), result.getName());

        Mockito.verify(mockRepository, Mockito.times(1))
                .findByName(mockTagDto.getName());

    }

    @Test
    public void getByName_Should_Throw_When_NameNotFound() {

        Mockito.when(mockRepository.findByName(mockTagDto.getName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> mockService.getByName(mockTagDto.getName()));
    }

    @Test
    public void editTag_Should_editAndSave_WhenValid() {

        Tag realTag = new Tag();
        realTag.setName(mockTagDto.getName());

        Mockito.when(mockRepository.findById(1L))
                .thenReturn(Optional.of(realTag));

        Mockito.when(mockRepository.findByName(mockTagDto.getName()))
                .thenReturn(Optional.empty());

        mockService.editTag(1L, mockTagDto);

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(realTag);

    }

    @Test
    public void editTag_Should_Throw_WhenTagNameExist() {

        Mockito.when(mockRepository.findById(1L))
                .thenReturn(Optional.of(Tag.builder().id(1L).build()));

        Tag conflictTag = Tag.builder()
                .id(2L)
                .name(mockTagDto.getName())
                .build();

        Mockito.when(mockRepository.findByName(mockTagDto.getName()))
                .thenReturn(Optional.of(conflictTag));

        Assertions.assertThrows(EntityExistsException.class, () -> mockService.editTag(1L, mockTagDto));
    }

    @Test
    public void getAll_Should_Return_ListOfTags() {

        Tag tag1 = Tag.builder().id(1L).name("test").build();
        Tag tag2 = Tag.builder().id(2L).name("test2").build();

        Mockito.when(mockRepository.findAll())
                .thenReturn(List.of(tag1, tag2));

        Set<Tag> result = mockService.getAll();

        Mockito.verify(mockRepository,Mockito.times(1))
                .findAll();

        Assertions.assertEquals(2, result.size());
    }

}
