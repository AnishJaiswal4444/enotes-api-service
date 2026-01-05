package Enotes_API_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotesRequest {

    private String title;

    private String description;

    private NotesDto.CategoryDto category;

}
