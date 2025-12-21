package Enotes_API_Service.service;

import Enotes_API_Service.Dto.NotesDto;

import java.util.List;

public interface NotesService {

    public Boolean saveNotes(NotesDto notesDto) throws Exception;

    public List<NotesDto> getAllNotes();
}
