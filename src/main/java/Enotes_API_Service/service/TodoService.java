package Enotes_API_Service.service;

import Enotes_API_Service.Dto.TodoDto;

import java.util.List;

public interface TodoService {

    public  Boolean saveTodo(TodoDto todoDto)throws Exception;

    public TodoDto getTodoById(Integer id) throws Exception;

    public List<TodoDto> getTodoByUser() throws Exception;
}
