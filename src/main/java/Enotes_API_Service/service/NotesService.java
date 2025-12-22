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

    public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

    public void softDeleteNotes(Integer id) throws Exception;

    public void restoreNotes(Integer id)throws  Exception;

    public List<NotesDto> getUserRecycleBinNotes(Integer userId);

    public void hardDeleteNotes(Integer id) throws Exception;

    public void emptyRecycleBin(int userId);

    public void favouriteNote(Integer noteId) throws Exception;

    public void unFavouriteNote(Integer noteId) throws Exception;

    public List<FavouriteNoteDto> getUserFavouriteNote();
}
