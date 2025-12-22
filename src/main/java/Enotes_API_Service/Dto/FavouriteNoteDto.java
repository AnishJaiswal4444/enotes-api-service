package Enotes_API_Service.Dto;

import Enotes_API_Service.entity.Notes;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavouriteNoteDto {
    private  Integer id;

    private Notes notes;

    private Integer userId;
}
