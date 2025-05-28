package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.example.Hospital_Management_System.dto.PatientDTO;
import com.example.Hospital_Management_System.dto.PatientSaveDTO;
import com.example.Hospital_Management_System.exception.ResourceNotFoundException;
import com.example.Hospital_Management_System.model.Patient;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.repo.PatientRepo;
import com.example.Hospital_Management_System.service.GrantAccess;
import com.example.Hospital_Management_System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Tag(name = "Patient APIs", description = "New Register, FindAll, Find-By-Id")
public class PatientController {
    @Autowired
    private UserService userService;
    @Autowired
    private GrantAccess grantAccess;
    @Autowired
    private PatientRepo patientRepo;
    //permit all
    @Operation(
            summary = "Register a new patient",
            description = "Registers a new patient by creating both user and patient records. Accessible by everyone."
    )

    @PostMapping("/patient/register")
    public ResponseEntity<PatientDTO> save(@Valid @RequestBody PatientSaveDTO patientSaveDTO){
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(5);
       User user= new User();
       user.setPassword(encoder.encode(patientSaveDTO.getPassword()));
       user.setName(patientSaveDTO.getName());
       user.setRole(patientSaveDTO.getRole());
       user.setEmail(patientSaveDTO.getEmail());

       Patient patient= new Patient();
       patient.setAge(patientSaveDTO.getAge());
       patient.setGender(patientSaveDTO.getGender());
       patient.setUser(user);
       user.setPatient(patient);
       userService.save(user);   //Behind the scene .save(user) is setting the Id of user and doctor
        //before saving to database so after saving if we do user.getId() it return he valid id in database
        return ResponseEntity.ok(createPatientDTO(user));
    }
   //admin only
   @Operation(
           summary = "Get all patients",
           description = "Fetches a list of all registered patients. Accessible only by admin users."
   )

    @GetMapping("/patient/findall")
    public ResponseEntity<List<PatientDTO>> findAll(){
        List<PatientDTO> patientDTOList= userService.findAll().stream()
                .filter((user)->
                        user.getRole()== USER_ROLE.PATIENT && user.getPatient()!=null
                        ).map((user)-> {
                            return createPatientDTO(user);
                        }
                        ).collect(Collectors.toList());
        if(patientDTOList.size()==0){
            throw new ResourceNotFoundException("Patients List is Empty");
        }
        return ResponseEntity.ok(patientDTOList);
    }
//admin, doctor and that specific patient
@Operation(
        summary = "Find patient by ID",
        description = "Fetches patient information by patient ID. Accessible by admin,or the patient themselves."
)

@GetMapping("/patient/{patientId}")
    public ResponseEntity<PatientDTO> findById(@PathVariable int patientId)throws AccessDeniedException{
        User user= grantAccess.getAuthenticationUser();
        boolean isAdmin = user.getRole() == USER_ROLE.ADMIN;
        boolean isPatient= user.getPatient()!=null;
        System.out.println(isAdmin);
        if (isAdmin || (isPatient && user.getPatient().getId()==patientId))
        {
            Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient Not Found"));
            return ResponseEntity.ok(createPatientDTO(patient.getUser()));
        }
        throw new AccessDeniedException("Sorry, Accessed Denied");
    }

    private PatientDTO createPatientDTO(User user) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName(user.getName());
        patientDTO.setEmail(user.getEmail());
        patientDTO.setRole(user.getRole().name());
        patientDTO.setUserId(user.getId());
        patientDTO.setPatientId(user.getPatient().getId());
        patientDTO.setAge(user.getPatient().getAge());
        patientDTO.setGender((user.getPatient().getGender()));
        patientDTO.setUserId(user.getId());
        return patientDTO;
    }
}
