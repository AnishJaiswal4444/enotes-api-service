package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.Dto.NotesRequest;
import Enotes_API_Service.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static Enotes_API_Service.util.Constants.*;

@Tag(name = "Notes", description = "Complete notes management API - create, organize, and manage user notes")
@RequestMapping("/api/v1/notes")
public interface NotesEndpoint {

    @Operation(summary = "Create Note", description = "User - Create a new note with optional file attachment", tags = {"Notes"})
    @PostMapping(value = "/", consumes = "multipart/form-data")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> saveNotes(@RequestParam @Parameter(description = "JSON String Notes"
                                                   , required = true
                                                   , content = @Content(schema = @Schema(implementation = NotesRequest.class)))
                                           String notes, @RequestParam(required = false) MultipartFile file) throws Exception;

    @Operation(summary = "Download Note Attachment", description = "Admin/User - Download file attached to a note", tags = {"Notes"})
    @GetMapping("download/{id}")
    @PreAuthorize(ROLE_ADMIN_USER)
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Get All Notes", description = "Admin only - Retrieve all notes across all users", tags = {"Notes"})
    @GetMapping("/")
    @PreAuthorize(Constants.ROLE_ADMIN)
    public ResponseEntity<?> getAllNotes();

    @Operation(summary = "Get User Notes", description = "User - Get paginated list of current user's notes", tags = {"Notes"})
    @GetMapping("/user-notes")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

    @Operation(summary = "Search Notes", description = "User - Search notes by keyword with pagination", tags = {"Notes"})
    @GetMapping("/search")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> searchNotes(@RequestParam(name = "key", defaultValue = "") String key,
                                         @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

    @Operation(summary = "Soft Delete Note", description = "User - Move note to recycle bin (soft delete)", tags = {"Notes"})
    @GetMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Restore Note", description = "User - Restore note from recycle bin", tags = {"Notes"})
    @GetMapping("/restore/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Get Recycle Bin", description = "User - Retrieve all notes in user's recycle bin", tags = {"Notes"})
    @GetMapping("/recycle-bin")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;

    @Operation(summary = "Permanently Delete Note", description = "User - Permanently delete a note (hard delete)", tags = {"Notes"})
    @DeleteMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Empty Recycle Bin", description = "User - Permanently delete all notes in recycle bin", tags = {"Notes"})
    @DeleteMapping("/delete")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> emptyUserRecycleBin() throws Exception;

    @Operation(summary = "Add to Favorites", description = "User - Mark a note as favorite", tags = {"Notes"})
    @GetMapping("/fav/{noteId}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception;

    @Operation(summary = "Remove from Favorites", description = "User - Remove note from favorites", tags = {"Notes"})
    @DeleteMapping("/un-fav/{favNoteId}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception;

    @Operation(summary = "Get Favorite Notes", description = "User - Retrieve all favorite notes", tags = {"Notes"})
    @GetMapping("/fav-note")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getUserFavouriteNote() throws Exception;

    @Operation(summary = "Copy Note", description = "User - Create a duplicate copy of an existing note", tags = {"Notes"})
    @GetMapping("/copy/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> copyNote(@PathVariable Integer id) throws Exception;
}