package com.camcyber.service.serviceimps;

import com.camcyber.dtos.requests.UserRequest;
import com.camcyber.dtos.responses.BaseResponseSuccess;
import com.camcyber.dtos.responses.UserResponse;
import com.camcyber.entities.FileEntity;
import com.camcyber.entities.UserEntity;
import com.camcyber.entities.UsersTypeEntity;
import com.camcyber.repositories.FileRepository;
import com.camcyber.repositories.UserRepository;
import com.camcyber.repositories.UsersTypeRepository;
import com.camcyber.service.UserService;
import com.camcyber.shares.FileUtil;
import com.camcyber.shares.exception.BadRequestException;
import com.camcyber.shares.exception.InternalServerException;
import com.camcyber.shares.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UsersTypeRepository usersTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileRepository fileRepository;
    @Value("${file.upload-dir}")
    private String UPLOAD_DIR ;

    @Override
    @Transactional
    public void create(MultipartFile file,UserRequest request, UserDetails userDetails) {
        String uniqueCode = StringUtils.cleanPath(Objects.requireNonNull
                (Objects.requireNonNull(file.getOriginalFilename()).replaceFirst("[.][^.]+$", ""))) + "_" + System.currentTimeMillis();
        FileEntity fileEntity1;
        try {
            // Save the file to the specified storage directory
            Path filePath = Paths.get(UPLOAD_DIR + uniqueCode.concat(".".concat(Objects.requireNonNull(FileUtil.getFileExtension(file)))));
            Files.copy(file.getInputStream(), filePath);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(FileUtil.getFileExtension(file));
            fileEntity.setCode(uniqueCode);
            fileEntity1 = fileRepository.save(fileEntity);


        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error file upload failed.");
        }

        UserEntity user = new UserEntity();
        user.setId(2);
        user.setName(request.getName());
//        if (Objects.nonNull(userRepository.findByPhone(request.getPhone())))
//            throw new BadRequestException("The phone number already exist.");
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setFileEntity(fileEntity1);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        UsersTypeEntity usersType = usersTypeRepository.findById(request.getUserType())
                .orElseThrow(()->new NotFoundException("User type not found."));

        user.setUsersType(usersType);
        try {
            user.setCreator(userRepository.findByPhone(userDetails.getUsername())
                    .orElseThrow(()->new NotFoundException("User not found.")));
            user.setCreatedAt(LocalDateTime.now());

            userRepository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error.");
        }
    }

    @Override
    public List<UserResponse> list() {

        List<UserResponse> userResponses = new ArrayList<>();
        List<UserEntity> userEntities;
        try {
            userEntities = userRepository.findAllByIsDeletedFalse();
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error.");
        }

        for (UserEntity user : userEntities){
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setName(user.getName());
            userResponse.setPhone(user.getPhone());
            userResponse.setEmail(user.getEmail());
            // Generate download link
            String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(user.getFileEntity().getCode().concat(".").concat(user.getFileEntity().getName()))
                    .toUriString();
            userResponse.setAvatar(downloadLink);
            userResponses.add(userResponse);

        }
        return userResponses;
    }

    @Override
    public UserResponse detail(Integer id) {

        UserEntity user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new NotFoundException("User Not Found."));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(user.getFileEntity().getCode().concat(".").concat(user.getFileEntity().getName()))
                .toUriString();
        userResponse.setAvatar(downloadLink);

        return userResponse;
    }

    @Override
    @Transactional
    public void update(MultipartFile file,Integer id, UserRequest request,UserDetails userDetails) throws IOException {

        UserEntity user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new NotFoundException("User Not Found."));
        Boolean isUpdated = Boolean.FALSE;

        if (Objects.nonNull(file)){
            if (Objects.equals(user.getPhone(),userDetails.getUsername()))
                throw new BadRequestException("Anonymous User.");
            String uniqueCode = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())) + "_" + System.currentTimeMillis();
            FileEntity fileEntity1;
            Path filePath = Paths.get(UPLOAD_DIR + uniqueCode);
            Files.copy(file.getInputStream(), filePath);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(FileUtil.getFileExtension(file));
            fileEntity.setCode(uniqueCode);
            fileEntity1 = fileRepository.save(fileEntity);
            user.setFileEntity(fileEntity1);
            isUpdated = Boolean.TRUE;
        }

        if (Objects.nonNull(request.getName())){
            user.setName(request.getName());
            isUpdated = Boolean.TRUE;
        }
        if (Objects.nonNull(request.getPhone())){
            user.setName(request.getPhone());
            isUpdated = Boolean.TRUE;
        }

        if (Objects.nonNull(request.getEmail())){
            user.setName(request.getEmail());
            isUpdated = Boolean.TRUE;
        }

        if (Objects.nonNull(request.getPassword())){
            if (Objects.equals(user.getPhone(),userDetails.getUsername()))
                throw new BadRequestException("Anonymous User.");
            user.setName(passwordEncoder.encode(request.getPassword()));
            isUpdated = Boolean.TRUE;
        }
        try {
            if (isUpdated){
                user.setUpdater(userRepository.findByPhone(userDetails.getUsername())
                        .orElseThrow(()->new NotFoundException("User not found.")));
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            }else throw new BadRequestException("Nothing Update.");
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error.");
        }
    }

    @Override
    @Transactional
    public void delete(Integer id,UserDetails userDetails) {

        UserEntity user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()->new NotFoundException("User not found."));
        user.setIsDeleted(Boolean.TRUE);
        user.setDeleter(userRepository.findByPhone(userDetails.getUsername())
                .orElseThrow(()->new NotFoundException("User ot found.")));
        user.setDeletedAt(LocalDateTime.now());

        try {
            userRepository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerException("Internal Server Error.");
        }

    }
}
