package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.NotesDto;
import Enotes_API_Service.entity.Notes;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.repository.CategoryRepository;
import Enotes_API_Service.repository.NotesRepository;
import Enotes_API_Service.service.NotesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesRepository notesRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Boolean saveNotes(NotesDto notesDto) throws Exception{

        //Validation
        checkCategoryExist(notesDto.getCategory());

        Notes notes = mapper.map(notesDto, Notes.class);

        Notes saveNotes = notesRepo.save(notes);

        if(!ObjectUtils.isEmpty(saveNotes)){
            return true;
        }
        return null;
    }

    private void checkCategoryExist(NotesDto.CategoryDto category) throws Exception {
        categoryRepository.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category is invalid"));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        return notesRepo.findAll().stream().map(notes -> mapper.map(notes, NotesDto.class)).toList();
    }
}
