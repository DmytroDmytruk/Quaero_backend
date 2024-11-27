package org.startup.quaero.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.startup.quaero.service.SelectionProcessService;

@RestController
public class SelectionProcessController {
    @Autowired
    private SelectionProcessService selectionProcessService;
}
