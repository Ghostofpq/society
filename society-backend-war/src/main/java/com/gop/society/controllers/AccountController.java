package com.gop.society.controllers;

import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/accounts")
@Component("accountController")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostConstruct
    private void init() {
        log.info("AccountController started !");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Account find(
            @RequestParam("owner") final String owner,
            @RequestParam("currency") final String currency)
            throws CustomNotFoundException {
        return accountService.findByOwnerAndCurrency(owner, currency);
    }
}
