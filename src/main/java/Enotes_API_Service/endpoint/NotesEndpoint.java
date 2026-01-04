package Enotes_API_Service.endpoint;

import Enotes_API_Service.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static Enotes_API_Service.util.Constants.*;

@RequestMapping("/api/v1/notes")
public interface NotesEndpoint {

    @PostMapping("/")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam (required = false) MultipartFile file) throws Exception;

    @GetMapping("download/{id}")
    @PreAuthorize(ROLE_ADMIN_USER)
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;

    @GetMapping("/")
    @PreAuthorize(Constants.ROLE_ADMIN)
    public ResponseEntity<?> getAllNotes();

    @GetMapping("/user-notes")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam (name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam (name = "pageSize", defaultValue =  DEFAULT_PAGE_SIZE)Integer pageSize);

    @GetMapping("/search")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> searchNotes( @RequestParam(name = "key", defaultValue = "") String key,
                                          @RequestParam (name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                          @RequestParam (name = "pageSize", defaultValue =  DEFAULT_PAGE_SIZE)Integer pageSize);

    @GetMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/restore/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/recycle-bin")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @DeleteMapping("/delete")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> emptyUserRecycleBin() throws Exception;

    @GetMapping("/fav/{noteId}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception;

    @DeleteMapping("/un-fav/{favNoteId}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception;

    @GetMapping("/fav-note")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getUserFavouriteNote() throws Exception;

    @GetMapping("/copy/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> copyNote(@PathVariable Integer id) throws Exception;
}
