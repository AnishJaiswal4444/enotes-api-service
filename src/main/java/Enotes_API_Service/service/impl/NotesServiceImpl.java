package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.entity.FileDetails;
import Enotes_API_Service.entity.Notes;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.repository.CategoryRepository;
import Enotes_API_Service.repository.FileRepository;
import Enotes_API_Service.repository.NotesRepository;
import Enotes_API_Service.service.NotesService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws Exception{

        ObjectMapper ob = new ObjectMapper();
        NotesDto notesDto = ob.readValue(notes, NotesDto.class);

        //Validation
        checkCategoryExist(notesDto.getCategory());

        Notes notesMap = mapper.map(notesDto, Notes.class);

        FileDetails fileDetails = saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)){
            notesMap.setFileDetails(fileDetails);
        }else{
            notesMap.setFileDetails(null);
        }

        Notes saveNotes = notesRepo.save(notesMap);

        if(!ObjectUtils.isEmpty(saveNotes)){
            return true;
        }
        return false;
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
}
