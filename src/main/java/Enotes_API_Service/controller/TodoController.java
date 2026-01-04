package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.TodoDto;
import Enotes_API_Service.endpoint.TodoEndpoint;
import Enotes_API_Service.service.TodoService;
import Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController implements TodoEndpoint {

    @Autowired
    private TodoService todoService;

    @Override
    public ResponseEntity<?> saveTodo (@RequestBody TodoDto todoDto) throws Exception{
        Boolean saveTodo = todoService.saveTodo(todoDto);
        if(saveTodo){
            return CommonUtil.createBuildResponseMessage("Todo is created successfully", HttpStatus.CREATED);
        }else{
            return CommonUtil.createErrorResponseMessage("Unable to create Todo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getTodo (@PathVariable Integer id) throws Exception {
        TodoDto todoDto = todoService.getTodoById(id);
        return CommonUtil.createBuildResponse(todoDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllTodoByUser () throws Exception {
        List<TodoDto> todoByUser = todoService.getTodoByUser();
        if(!CollectionUtils.isEmpty(todoByUser)){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(todoByUser, HttpStatus.OK);
    }
}
