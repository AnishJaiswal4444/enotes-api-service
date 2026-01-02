package Enotes_API_Service.service;

import Enotes_API_Service.Dto.FavouriteNoteDto;
import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.Dto.NotesResponse;
import Enotes_API_Service.entity.FileDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotesService {

    public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

    public List<NotesDto> getAllNotes();

    public byte[] downloadFile(FileDetails fileDetails) throws Exception;

    public FileDetails getFileDetails(Integer id) throws Exception;

    public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize);

    public void softDeleteNotes(Integer id) throws Exception;

    public void restoreNotes(Integer id)throws  Exception;

    public List<NotesDto> getUserRecycleBinNotes();

    public void hardDeleteNotes(Integer id) throws Exception;

    public void emptyRecycleBin();

    public void favouriteNote(Integer noteId) throws Exception;

    public void unFavouriteNote(Integer noteId) throws Exception;

    public List<FavouriteNoteDto> getUserFavouriteNote();

    public Boolean copyNote(Integer id) throws  Exception;
}
