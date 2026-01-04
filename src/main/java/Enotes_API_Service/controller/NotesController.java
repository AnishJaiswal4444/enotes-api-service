package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.FavouriteNoteDto;
import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.Dto.NotesResponse;
import Enotes_API_Service.endpoint.NotesEndpoint;
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
public class NotesController implements NotesEndpoint {

    @Autowired
    private NotesService notesService;

    @Override
    public ResponseEntity<?> saveNotes( String notes, MultipartFile file) throws Exception {
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if(saveNotes){
            return CommonUtil.createBuildResponseMessage("Notes saved successfully", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> downloadFile( Integer id) throws Exception {

        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data = notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();

        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return ResponseEntity.ok().headers(headers).body(data);
    }

    @Override
    public ResponseEntity<?> getAllNotes(){
        List<NotesDto> notes = notesService.getAllNotes();
        if(CollectionUtils.isEmpty(notes)){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllNotesByUser(
             Integer pageNo,
             Integer pageSize){
        NotesResponse notes = notesService.getAllNotesByUser(pageNo, pageSize);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchNotes(  String key,
             Integer pageNo,
             Integer pageSize){
        NotesResponse notes = notesService.getAllNotesBySearch(pageNo, pageSize, key);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteNotes( Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> restoreNotes( Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Restored Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        List<NotesDto> notes = notesService.getUserRecycleBinNotes();
        if(CollectionUtils.isEmpty(notes)){
            return CommonUtil.createBuildResponseMessage("Notes not available in Recycle Bin", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> hardDeleteNotes( Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> emptyUserRecycleBin() throws Exception {
        notesService.emptyRecycleBin();
        return CommonUtil.createBuildResponseMessage("Delete Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> favouriteNote( Integer noteId) throws Exception {
        notesService.favouriteNote(noteId);
        return CommonUtil.createBuildResponseMessage("Note added to Favourite", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> unFavouriteNote( Integer favNoteId) throws Exception {
        notesService.unFavouriteNote(favNoteId);
        return CommonUtil.createBuildResponseMessage("Note Removed from Favourite", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserFavouriteNote() throws Exception {
        List<FavouriteNoteDto> userFavouriteNotes = notesService.getUserFavouriteNote();
        if (CollectionUtils.isEmpty(userFavouriteNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavouriteNotes, HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<?> copyNote( Integer id) throws Exception {
        Boolean copyNotes = notesService.copyNote(id);
        if(copyNotes) {
            return CommonUtil.createBuildResponseMessage("Note has been copied", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Copy failed ! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
