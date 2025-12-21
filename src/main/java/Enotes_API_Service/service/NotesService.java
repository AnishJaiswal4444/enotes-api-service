package Enotes_API_Service.service;

import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.entity.FileDetails;
import Enotes_API_Service.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotesService {

    public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

    public List<NotesDto> getAllNotes();

    public byte[] downloadFile(FileDetails fileDetails) throws Exception;

    FileDetails getFileDetails(Integer id) throws Exception;

}
