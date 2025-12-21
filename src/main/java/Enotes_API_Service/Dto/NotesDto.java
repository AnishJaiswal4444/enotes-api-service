package Enotes_API_Service.Dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotesDto {

    private Integer id;

    private String title;

    private String description;

    private CategoryDto category;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CategoryDto{
        private Integer id;

        private String title;
    }
}
