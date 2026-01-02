package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.FavouriteNoteDto;
import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.Dto.NotesResponse;
import Enotes_API_Service.entity.FavouriteNote;
import Enotes_API_Service.entity.FileDetails;
import Enotes_API_Service.entity.Notes;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.repository.CategoryRepository;
import Enotes_API_Service.repository.FavouriteNoteRepository;
import Enotes_API_Service.repository.FileRepository;
import Enotes_API_Service.repository.NotesRepository;
import Enotes_API_Service.service.NotesService;
import Enotes_API_Service.util.CommonUtil;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesRepository notesRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FavouriteNoteRepository favouriteNoteRepository;

    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws Exception{

        ObjectMapper ob = new ObjectMapper();
        NotesDto notesDto = ob.readValue(notes, NotesDto.class);
        notesDto.setIsDeleted(false);
        notesDto.setDeletedOn(null);
        if(!ObjectUtils.isEmpty(notesDto.getId())){
            updateNotes(notesDto, file);
        }
        //Validation
        checkCategoryExist(notesDto.getCategory());

        Notes notesMap = mapper.map(notesDto, Notes.class);

        FileDetails fileDetails = saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)){
            notesMap.setFileDetails(fileDetails);
        }else{
            if(ObjectUtils.isEmpty(notesDto.getId())){
                notesMap.setFileDetails(null);
            }
        }

        Notes saveNotes = notesRepo.save(notesMap);

        if(!ObjectUtils.isEmpty(saveNotes)){
            return true;
        }
        return false;
    }

    private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception{
        Notes existNotes = notesRepo.findById(notesDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Invalid Notes Id"));
        if(ObjectUtils.isEmpty(file)){
            notesDto.setFileDetails(mapper.map(existNotes.getFileDetails(), NotesDto.FilesDto.class));
        }
    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException {

        if(!ObjectUtils.isEmpty(file) && !file.isEmpty()){

            String originalFileName = file.getOriginalFilename();

            String rndString = UUID.randomUUID().toString();
            String extension = FilenameUtils.getExtension(originalFileName);
            String uploadFileName = rndString + "." + extension;

            File saveFile = new File(uploadPath);

            if(!saveFile.exists()){
                saveFile.mkdir();
            }
            // path: enotes-api-service/notes/java.pdf
            String storePath = uploadPath.concat(uploadFileName);

            //upload file
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if(upload != 0){
                FileDetails fileDetails = new FileDetails();
                fileDetails.setOriginalFileName(originalFileName);
                fileDetails.setDisplayFileName(getDisplayName(originalFileName));
                fileDetails.setUploadFileName(uploadFileName);
                fileDetails.setFileSize(file.getSize());
                fileDetails.setPath(storePath);
                FileDetails saveFileDetails = fileRepository.save(fileDetails);
                return saveFileDetails;
            }

        }
        return null;
    }

    private String getDisplayName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.removeExtension(originalFileName);
        if(fileName.length() > 8){
            fileName = fileName.substring(0,7);
        }
        fileName = fileName + "." + extension;
        return fileName;
    }

    private void checkCategoryExist(NotesDto.CategoryDto category) throws Exception {
        categoryRepository.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category is invalid"));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        return notesRepo.findAll().stream().map(notes -> mapper.map(notes, NotesDto.class)).toList();
    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception{
        InputStream io = new FileInputStream(fileDetails.getPath());
        return StreamUtils.copyToByteArray(io);
    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception{
        FileDetails fileDetails = fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File is not available"));
        return fileDetails;
    }

    @Override
    public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize) {
        // if 10 items = 5, 5 --> 2 pages
        Integer userId = CommonUtil.getLoggedInUser().getId();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Notes> pageNotes = notesRepo.findByCreatedByAndIsDeletedFalse(userId, pageable);
        List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();
        NotesResponse notes = NotesResponse.builder()
                .notes(notesDto)
                .pageNo(pageNotes.getNumber())
                .pageSize(pageNotes.getSize())
                .totalElements(pageNotes.getTotalElements())
                .totalPages(pageNotes.getTotalPages())
                .isFirst(pageNotes.isFirst())
                .isLast(pageNotes.isLast())
                .build();
        return notes;
    }

    @Override
    public void softDeleteNotes(Integer id) throws Exception {
        Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes Id not found"));
        notes.setIsDeleted(true);
        notes.setDeletedOn(LocalDateTime.now());
        notesRepo.save(notes);
    }

    @Override
    public void restoreNotes(Integer id) throws  Exception {
        Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes Id not found"));
        notes.setIsDeleted(false);
        notes.setDeletedOn(null);
        notesRepo.save(notes);
    }

    @Override
    public List<NotesDto> getUserRecycleBinNotes() {
        Integer userId = CommonUtil.getLoggedInUser().getId();
        List<Notes> recycleNotes = notesRepo.findByCreatedByAndIsDeletedTrue(userId);
        List<NotesDto> notesDtoList = recycleNotes.stream().map(note -> mapper.map(note, NotesDto.class)).toList();
        return notesDtoList;
    }

    @Override
    public void hardDeleteNotes(Integer id) throws Exception {
        Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes not found"));
        //Only delete if the notes are in recycle bin
        if(notes.getIsDeleted()){
            notesRepo.delete(notes);
        }else{
            throw new IllegalArgumentException("Sorry you can't hard delete directly");
        }
    }

    @Override
    public void emptyRecycleBin() {
        Integer userId = CommonUtil.getLoggedInUser().getId();
        List<Notes> recycleNotes = notesRepo.findByCreatedByAndIsDeletedTrue(userId);
        if(!CollectionUtils.isEmpty(recycleNotes)){
            notesRepo.deleteAll(recycleNotes);
        }
    }

    @Override
    public void favouriteNote(Integer noteId) throws Exception {
        int userId = 1;
        Notes notes = notesRepo.findById(noteId).orElseThrow(() -> new ResourceNotFoundException("Notes not found"));
        FavouriteNote favouriteNote = FavouriteNote.builder()
                .notes(notes)
                .userId(userId)
                .build();
        favouriteNoteRepository.save(favouriteNote);
    }

    @Override
    public void unFavouriteNote(Integer favouriteNoteId) throws Exception {
        FavouriteNote favouriteNote = favouriteNoteRepository.findById(favouriteNoteId).orElseThrow(() -> new ResourceNotFoundException("Notes not found"));
        favouriteNoteRepository.delete(favouriteNote);
    }

    @Override
    public List<FavouriteNoteDto> getUserFavouriteNote() {
        Integer userId = CommonUtil.getLoggedInUser().getId();
        List<FavouriteNote> favouriteNotes = favouriteNoteRepository.findByUserId(userId);
        return favouriteNotes.stream().map(fn -> mapper.map(fn, FavouriteNoteDto.class)).toList();
    }

    @Override
    public Boolean copyNote(Integer id) throws Exception{
        Notes notes = notesRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note is not available"));

        // TODO: Validation: user can only copy their notes

        Notes copyNote = Notes.builder()
                .title(notes.getTitle())
                .description(notes.getDescription())
                .category(notes.getCategory())
                .isDeleted(false)
                .fileDetails(null)
                .build();
        Notes saveCopyNotes = notesRepo.save(copyNote);
        if(!ObjectUtils.isEmpty(saveCopyNotes)){
            return true;
        }
        return false;
    }
}
