package com.galvanize.badgevisitor.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class VisitorExtended {
    private Long id;

    private Long phoneNumber;

    private String firstName;
    private String lastName;
    private String company;

    private String hostName;
    private Long hostPhone;
    private String purposeOfVisit;

    private String checkedInBy;
    private String checkedOutBy;
    private String reasonForDeletion;
    private String badgeNumber;

    private Date registerDate;
    private Date checkedInDate;
    private Date checkedOutDate;
    private Long milliSecondsSinceRegistration;

    private Boolean active;
    private VisitStatus status;

    @Builder
    public VisitorExtended(Long id, Long phoneNumber, String firstName, String lastName, String company,
                           String hostName, Long hostPhone, String purposeOfVisit, String checkedInBy,
                           String checkedOutBy, String reasonForDeletion, String badgeNumber,
                           Date registerDate, Date checkedInDate, Date checkedOutDate, Boolean active, VisitStatus status) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.hostName = hostName;
        this.hostPhone = hostPhone;
        this.purposeOfVisit = purposeOfVisit;
        this.checkedInBy = checkedInBy;
        this.checkedOutBy = checkedOutBy;
        this.reasonForDeletion = reasonForDeletion;
        this.badgeNumber = badgeNumber;
        this.registerDate = registerDate;
        this.checkedInDate = checkedInDate;
        this.checkedOutDate = checkedOutDate;
        this.active = active;
        this.status = status;
    }
}