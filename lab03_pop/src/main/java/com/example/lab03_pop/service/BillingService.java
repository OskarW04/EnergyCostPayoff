package com.example.lab03_pop.service;

import com.example.lab03_pop.auth.CustomUserDetails;
import com.example.lab03_pop.entity.*;
import com.example.lab03_pop.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final TaskRepository taskRepository;
    private final PaymentRepository paymentRepository;
    private final BuildingRepository buildingRepository;

    @Transactional
    public String createPayment(int taskId, Double costPerkWh){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.getUser();

        if (!currentUser.getRole().equals(Role.MANAGER)) {
            throw new IllegalStateException("Only managers can create payments");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));


        if(!task.isCompleted()){
            throw new IllegalArgumentException("Task not completed");
        }

        Building building = task.getBuilding();
        if(building == null){
            throw new IllegalArgumentException("Task does not have an associated building");
        }
        Double currentBuildingReading = building.getBuildingReading();
        Double previousBuildingReading = building.getPreviousBuildingReading();

        if (currentBuildingReading == null || previousBuildingReading == null) {
            throw new IllegalStateException("Building does not have valid reading values");
        }

        List<Property> properties = building.getProperties();
        if(properties == null){
            throw new IllegalArgumentException("Building does not have any associated properties");
        }
        int numberOfProperties = properties.size();
        double allPropertiesConsumptionSum = properties.stream()
                .mapToDouble(Property::getReadingValue)
                .sum();
        double allPropertiesPrevConsumptionSum = properties.stream()
                .mapToDouble(Property::getPreviousReadingValue)
                .sum();

        double AllPropertiesConsumption = allPropertiesConsumptionSum - allPropertiesPrevConsumptionSum;


        for(Property property : properties){
            Tenant tenant = property.getTenant();
            if(tenant == null){
                continue;
            }
            double buildingConsumption = currentBuildingReading - previousBuildingReading;

            Double currentPropertyReading = property.getReadingValue();
            Double previousPropertyReading = property.getPreviousReadingValue();
            if (currentPropertyReading == null || previousPropertyReading == null) {
                throw new IllegalStateException("Property does not have valid reading values");
            }

            double propertyConsumption = currentPropertyReading - previousPropertyReading;
            Double cost = (propertyConsumption + ((buildingConsumption-AllPropertiesConsumption)/numberOfProperties))*costPerkWh;

            Payment payment = new Payment();
            payment.setAmount(cost);
            payment.setPaymentDate(LocalDate.now());
            payment.setTenant(tenant);
            payment.setProperty(property);
            payment.setPayed(false);
            paymentRepository.save(payment);

            property.setPreviousReadingValue(currentPropertyReading);
            property.setReadingValue(0.0);
        }

        building.setPreviousBuildingReading(currentBuildingReading);
        building.setBuildingReading(0.0);
        buildingRepository.save(building);

        task.setPaymentStatus(PaymentStatus.PENDING);
        taskRepository.save(task);

        return "Payment calculated succeeded";
    }

    public List<Payment> getAllPayments(){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("Only manager can do this");
        }

        return paymentRepository.findAllByManagerId(currentUser.getId());
    }

    @Transactional
    public String makePayment(int paymentId){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.TENANT)){
            throw new IllegalArgumentException("Only tenants can pay their bills");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        Tenant tenant = payment.getTenant();

        if(tenant.getId() != currentUser.getId()){
            throw new IllegalStateException("That is not this tenants payment");
        }

        if(payment.isPayed()){
            throw new IllegalStateException("Payment already payed");
        }
        payment.setPayed(true);
        tenant.getPaymentHistory().add(payment);



        paymentRepository.save(payment);

        List<Task> tasks = taskRepository.findByBuildingId(payment.getProperty().getBuilding().getId());
        boolean allPaymentsPaid = paymentRepository
                .findAllByBuildingId(payment.getProperty().getBuilding().getId())
                .stream()
                .allMatch(Payment::isPayed);

        if (allPaymentsPaid) {
            tasks.forEach(task -> {
                task.setPaymentStatus(PaymentStatus.PAID);
                taskRepository.save(task);
            });
        }

        return "Payment successfully registered";
    }

    public List<Payment> getPaymentHistory(){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.TENANT)){
            throw new IllegalArgumentException("Only tenant can see their payment history");
        }

        return paymentRepository.findByTenantIdAndPayedTrue(currentUser.getId());
    }

    public List<Payment> getCurrentPayments(){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.TENANT)){
            throw new IllegalArgumentException("Only tenant can see their payment history");
        }

        return paymentRepository.findByTenantIdAndPayedFalse(currentUser.getId());
    }

    @Transactional
    public String deletePayment(int paymentId){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("only manager can do this");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getProperty().getBuilding().getManager().getId() != currentUser.getId()) {
            throw new IllegalStateException("You do not manage the building associated with this payment");
        }

        paymentRepository.delete(payment);
        return "Payment deleted";
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}
