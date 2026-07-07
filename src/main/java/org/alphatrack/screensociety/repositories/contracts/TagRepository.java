package org.alphatrack.screensociety.repositories.contracts;

import org.alphatrack.screensociety.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {

}
