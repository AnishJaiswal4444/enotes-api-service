package Enotes_API_Service.util;

import Enotes_API_Service.Dto.CategoryDto;
import Enotes_API_Service.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Validation {
    public void categoryValidation(CategoryDto categoryDto){

        Map<String, Object> error  = new LinkedHashMap<>();
        if (ObjectUtils.isEmpty(categoryDto))
        {
            throw new IllegalArgumentException("category object shouldn't be null or empty");
        } else {

            // validation name field
            if(ObjectUtils.isEmpty(categoryDto.getName())){
                error.put("name", "name field is empty or null");
            }else{
                if(categoryDto.getName().length() < 3){
                    error.put("name", "name length is less then 3");
                }
                if(categoryDto.getName().length() > 100){
                    error.put("name", "name length is greater then 100");
                }
            }

            // validation description
            if(ObjectUtils.isEmpty(categoryDto.getDescription())){
                error.put("description", "description field is empty or null");
            }

            // validation isActive
            if(ObjectUtils.isEmpty(categoryDto.getIsActive())){
                error.put("isActive", "isActive field is empty or null");
            }
//            else{
//                if(categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive() != Boolean.FALSE.booleanValue()){
//                    error.put("isActive", "invalid value in isActive field");
//                }
//            }
        }
        if(!error.isEmpty()){
            throw new ValidationException(error);
        }
    }
}
