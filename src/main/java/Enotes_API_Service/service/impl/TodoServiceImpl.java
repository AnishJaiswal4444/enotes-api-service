package Enotes_API_Service.service.impl;
import Enotes_API_Service.enums.TodoStatus;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.util.Validation;
import org.modelmapper.ModelMapper;
import Enotes_API_Service.Dto.TodoDto;
import Enotes_API_Service.entity.Todo;
import Enotes_API_Service.repository.TodoRepository;
import Enotes_API_Service.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Validation validation;

    @Override
    public Boolean saveTodo(TodoDto todoDto) throws Exception{

        // validate todo status
        validation.todoValidation(todoDto);
        Todo todo = mapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());
        Todo save = todoRepository.save(todo);
        if(!ObjectUtils.isEmpty(save)){
            return true;
        }
        return false;
    }

    @Override
    public TodoDto getTodoById(Integer id)throws Exception {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo Not Found"));
        TodoDto todoDto = mapper.map(todo, TodoDto.class);
        setStatus(todoDto, todo);
        return todoDto;
    }

    private void setStatus(TodoDto todoDto, Todo todo) {
        for(TodoStatus st : TodoStatus.values()){
            if(st.getId().equals(todo.getStatusId())){
                TodoDto.StatusDto statusDto = TodoDto.StatusDto.builder()
                        .id(st.getId())
                        .name(st.getName())
                        .build();
                todoDto.setStatus(statusDto);
            }
        }
    }

    @Override
    public List<TodoDto> getTodoByUser() {
        Integer userId = 1;
        List<Todo> todoList = todoRepository.findByCreatedBy(userId);
        return todoList.stream().map(td -> mapper.map(td, TodoDto.class)).toList();
    }
}
