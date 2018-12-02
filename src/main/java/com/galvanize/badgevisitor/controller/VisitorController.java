package com.galvanize.badgevisitor.controller;

import com.galvanize.badgevisitor.entity.VisitorFrontEnd;
import com.galvanize.badgevisitor.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    private final VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @PostMapping("/register")
    public Boolean registerVisitor(@RequestBody VisitorFrontEnd visitor) {
        return visitorService.registerVisitor(visitor);
    }

    @GetMapping("/lookup/{phoneNumber}")
    public VisitorFrontEnd lookupVisitorByPhone(@PathVariable String phoneNumber) {
        return visitorService.findByPhoneNumber(phoneNumber);
    }

    @PutMapping("/checkout")
    public VisitorFrontEnd checkout(@RequestBody VisitorFrontEnd visitor) {
        return visitorService.checkout(visitor);
    }

}


