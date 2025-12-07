package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.handler.exception.UnauthorizedException;
import nijat.project.appointment.model.dto.request.PendingAppointmentRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.entity.AppointmentEntity;
import nijat.project.appointment.model.dto.shared.PendingAppointmentContextDto;
import nijat.project.appointment.model.entity.PendingAppointmentEntity;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.enums.AppointmentStatus;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.repository.AppointmentRepository;
import nijat.project.appointment.repository.PendingAppointmentRepository;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.EmailService;
import nijat.project.appointment.service.PendingAppointmentService;
import org.springframework.stereotype.Service;
import java.util.UUID;
import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@Service
@RequiredArgsConstructor
public class PendingAppointmentServiceImpl implements PendingAppointmentService {

    private final PendingAppointmentRepository pendingAppointmentRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    @Override
    public SuccessResponseDto<Void> requestAppointment(PendingAppointmentRequestDto pendingAppointmentRequestDto, String userId) {
        UUID id = parse(userId);
        UUID patientId = parse(pendingAppointmentRequestDto.getPatientId());
        UUID doctorId = parse(pendingAppointmentRequestDto.getDoctorId());

        if(userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User with this id: " + id + " not found");
        }

        if(!id.equals(patientId)){
            throw new UnauthorizedException("You are not authorized to perform this request");
        }

        UserEntity doctor = userRepository.findByIdAndUserRole(doctorId, UserRole.DOCTOR).orElseThrow(
                () -> new ResourceNotFoundException("Doctor with this id: " + doctorId + " not found"));
        UserEntity patient = userRepository.findByIdAndUserRole(patientId, UserRole.PATIENT).orElseThrow(
                () -> new ResourceNotFoundException("Patient with this id: " + patientId + " not found"));

        PendingAppointmentEntity pendingAppointment = PendingAppointmentEntity.builder()
                .doctorId(doctor.getId().toString())
                .patientId(patient.getId().toString())
                .status(AppointmentStatus.PENDING)
                .appointmentDate(pendingAppointmentRequestDto.getAppointmentDate())
                .appointmentTime(pendingAppointmentRequestDto.getAppointmentTime())
                .build();

        pendingAppointmentRepository.save(pendingAppointment);

        emailService.sendAppointmentRequest(doctor.getEmail(), doctor.getUsername(), patient.getUsername(),
                pendingAppointmentRequestDto.getAppointmentDate(), pendingAppointmentRequestDto.getAppointmentTime());

        return SuccessResponseDto.of( "The doctor will be notified of your appointment request");
    }

    @Override
    public SuccessResponseDto<Void> approveAppointment(String appointmentId, String userId) {
        PendingAppointmentContextDto pendingAppointmentContext = validateAndLoadPendingAppointmentContext(appointmentId, userId);
        UserEntity doctor = pendingAppointmentContext.getDoctor();
        UserEntity patient = pendingAppointmentContext.getPatient();
        PendingAppointmentEntity pendingAppointment = pendingAppointmentContext.getPendingAppointment();

        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .doctor(doctor)
                .patient(patient)
                .status(AppointmentStatus.SCHEDULED)
                .appointmentDate(pendingAppointment.getAppointmentDate())
                .appointmentTime(pendingAppointment.getAppointmentTime())
                .build();

        pendingAppointmentRepository.delete(pendingAppointment);
        appointmentRepository.save(appointmentEntity);

        emailService.sendAppointmentApproval(patient.getEmail(), doctor.getUsername(), patient.getUsername(),
                pendingAppointment.getAppointmentDate(), pendingAppointment.getAppointmentTime());

        return SuccessResponseDto.of("The appointment has been approved");
    }

    @Override
    public SuccessResponseDto<Void> rejectAppointment(String appointmentId, String userId) {
        PendingAppointmentContextDto pendingAppointmentContext = validateAndLoadPendingAppointmentContext(appointmentId, userId);
        UserEntity doctor = pendingAppointmentContext.getDoctor();
        UserEntity patient = pendingAppointmentContext.getPatient();
        PendingAppointmentEntity pendingAppointment = pendingAppointmentContext.getPendingAppointment();

        pendingAppointmentRepository.delete(pendingAppointment);

        emailService.sendAppointmentRejection(patient.getEmail(), doctor.getUsername(), patient.getUsername(),
                pendingAppointment.getAppointmentDate(), pendingAppointment.getAppointmentTime());

        return SuccessResponseDto.of("The appointment has been rejected");
    }

    public PendingAppointmentContextDto validateAndLoadPendingAppointmentContext(String appointmentId, String userId){
        UUID id = parse(userId);
        UUID parsedAppointmentId = parse(appointmentId);

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with this id: " + id + " not found")
        );
        PendingAppointmentEntity pendingAppointment = pendingAppointmentRepository.findById(parsedAppointmentId).orElseThrow(
                () -> new ResourceNotFoundException("No pending appointment with this id: " + parsedAppointmentId + " found")
        );

        UUID patientId = parse(pendingAppointment.getPatientId());
        UUID doctorId = parse(pendingAppointment.getDoctorId());

        UserEntity patient = userRepository.findByIdAndUserRole(patientId, UserRole.PATIENT).orElseThrow(
                () -> new ResourceNotFoundException("Patient not found")
        );
        UserEntity doctor = userRepository.findByIdAndUserRole(doctorId, UserRole.DOCTOR).orElseThrow(
                () -> new ResourceNotFoundException("Doctor not found")
        );

        if(!user.getId().equals(doctorId)){
            throw new UnauthorizedException("You are not authorized to perform this request");
        }

        return PendingAppointmentContextDto.builder()
                .doctor(doctor)
                .patient(patient)
                .pendingAppointment(pendingAppointment)
                .build();
    }
}
