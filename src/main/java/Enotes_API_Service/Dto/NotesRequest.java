package Enotes_API_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NotesRequest {

    private String title;

    private String description;

    private NotesDto.CategoryDto category;

}
