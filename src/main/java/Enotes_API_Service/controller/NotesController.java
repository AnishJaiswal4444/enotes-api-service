package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.FavouriteNoteDto;
import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.Dto.NotesResponse;
import Enotes_API_Service.entity.FileDetails;
import Enotes_API_Service.service.NotesService;
import Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam (required = false) MultipartFile file) throws Exception {
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if(saveNotes){
            return CommonUtil.createBuildResponseMessage("Notes saved successfully", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("download/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {

        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data = notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();

        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return ResponseEntity.ok().headers(headers).body(data);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllNotes(){
        List<NotesDto> notes = notesService.getAllNotes();
        if(CollectionUtils.isEmpty(notes)){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/user-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam (name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam (name = "pageSize", defaultValue =  "10")Integer pageSize){
        NotesResponse notes = notesService.getAllNotesByUser(pageNo, pageSize);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Restored Successfully", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        List<NotesDto> notes = notesService.getUserRecycleBinNotes();
        if(CollectionUtils.isEmpty(notes)){
            return CommonUtil.createBuildResponseMessage("Notes not available in Recycle Bin", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> emptyUserRecycleBin() throws Exception {
        notesService.emptyRecycleBin();
        return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
    }

    @GetMapping("/fav/{noteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception {
        notesService.favouriteNote(noteId);
        return CommonUtil.createBuildResponseMessage("Note added to Favourite", HttpStatus.CREATED);
    }

    @DeleteMapping("/un-fav/{favNoteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception {
        notesService.unFavouriteNote(favNoteId);
        return CommonUtil.createBuildResponseMessage("Note Removed from Favourite", HttpStatus.OK);
    }

    @GetMapping("/fav-note")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserFavouriteNote() throws Exception {
        List<FavouriteNoteDto> userFavouriteNotes = notesService.getUserFavouriteNote();
        if (CollectionUtils.isEmpty(userFavouriteNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavouriteNotes, HttpStatus.OK) ;
    }

    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> copyNote(@PathVariable Integer id) throws Exception {
        Boolean copyNotes = notesService.copyNote(id);
        if(copyNotes) {
            return CommonUtil.createBuildResponseMessage("Note has been copied", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Copy failed ! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
