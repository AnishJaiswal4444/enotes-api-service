package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.UserDto;
import Enotes_API_Service.entity.Role;
import Enotes_API_Service.entity.User;
import Enotes_API_Service.repository.RoleRepository;
import Enotes_API_Service.repository.UserRepository;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Validation validation;

    @Autowired
    private ModelMapper  mapper;

    @Override
    public Boolean register(UserDto userDto) {

        // Validate user role
        validation.userValidation(userDto);
        User user = mapper.map(userDto, User.class);
        setRole(userDto, user);
        User saveUser = userRepository.save(user);
        if(!ObjectUtils.isEmpty(saveUser)){
            return true;
        }
        return false;
    }

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }
}
