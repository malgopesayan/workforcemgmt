package com.example.workforcemgmt.model.enums;

import com.example.workforcemgmt.common.model.enums.ReferenceType;
// We are removing the @Getter import as we will write the methods ourselves.
// import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// @Getter has been removed.
public enum Task {
    ASSIGN_CUSTOMER_TO_SALES_PERSON(List.of(ReferenceType.ENTITY), "Assign customer to Sales person"),
    CREATE_INVOICE(List.of(ReferenceType.ORDER), "Create Invoice"),
    ARRANGE_PICKUP(List.of(ReferenceType.ORDER), "Arrange Pickup"),
    COLLECT_PAYMENT(List.of(ReferenceType.ORDER), "Collect Payment");

    private final List<ReferenceType> applicableReferenceTypes;
    private final String view;

    Task(List<ReferenceType> applicableReferenceTypes, String view) {
        this.applicableReferenceTypes = applicableReferenceTypes;
        this.view = view;
    }

    public static List<Task> getTasksByReferenceType(ReferenceType referenceType) {
        return Arrays.stream(Task.values())
                .filter(task -> task.getApplicableReferenceTypes().contains(referenceType)) // This line will now work
                .collect(Collectors.toList());
    }

    // ===============================================
    // MANUALLY ADD THESE METHODS TO FIX THE ERROR
    // ===============================================
    public List<ReferenceType> getApplicableReferenceTypes() {
        return this.applicableReferenceTypes;
    }

    public String getView() {
        return this.view;
    }
    // ===============================================
}