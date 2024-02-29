package com.camcyber.service.serviceimps;

import com.camcyber.dtos.requests.UserTypeRequest;
import com.camcyber.dtos.responses.UserTypeResponse;
import com.camcyber.entities.UsersTypeEntity;
import com.camcyber.repositories.UsersTypeRepository;
import com.camcyber.service.UserTypeService;
import com.camcyber.shares.exception.BadRequestException;
import com.camcyber.shares.exception.InternalServerException;
import com.camcyber.shares.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserTypeServiceImp implements UserTypeService {

    private final UsersTypeRepository usersTypeRepository;

    @Override
    public void create(UserTypeRequest request) {

        if (usersTypeRepository.existsByName(request.getName()))
            throw new BadRequestException("User type already existed.");
        UsersTypeEntity usersType = new UsersTypeEntity();
        usersType.setName(request.getName());
        try {
            usersTypeRepository.save(usersType);
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error");
        }


    }

    @Override
    public void update(Integer id, UserTypeRequest request) {

        if (usersTypeRepository.existsByName(request.getName()))
            throw new BadRequestException("User type already existed.");
        UsersTypeEntity usersType = usersTypeRepository.findById(id)
                        .orElseThrow(()->new NotFoundException("User type not found."));
        usersType.setName(request.getName());
        try {
            usersTypeRepository.save(usersType);
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error");
        }
    }

    @Override
    public UserTypeResponse detail(Integer id) {
        UsersTypeEntity usersType = usersTypeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User type not found."));
        UserTypeResponse response = new UserTypeResponse();
        response.setId(usersType.getId());
        response.setName(usersType.getName());
        return response;
    }

    @Override
    public List<UserTypeResponse> list() {
        List<UsersTypeEntity> usersTypeEntities = usersTypeRepository.findAll();
        List<UserTypeResponse> responses = new ArrayList<>();
        for (UsersTypeEntity usersType: usersTypeEntities){
            UserTypeResponse response = new UserTypeResponse();
            response.setId(usersType.getId());
            response.setName(usersType.getName());
            responses.add(response);
        }

        return responses;
    }

    @Override
    public void delete(Integer id) {
        UsersTypeEntity usersType = usersTypeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User type not found."));
        if (!usersType.getUsers().isEmpty())
            throw new BadRequestException("Cannot delete user types");
        usersTypeRepository.deleteById(usersType.getId());
    }
}
