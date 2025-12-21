package Enotes_API_Service.repository;

import Enotes_API_Service.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Notes, Integer> {
}
