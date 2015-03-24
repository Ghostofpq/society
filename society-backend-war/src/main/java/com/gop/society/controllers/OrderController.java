package com.gop.society.controllers;

import com.gop.society.exceptions.CustomBadRequestException;
import com.gop.society.exceptions.CustomNotAuthorizedException;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Account;
import com.gop.society.security.CustomAuthenticationProvider;
import com.gop.society.services.AccountService;
import com.gop.society.utils.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Controller
@RequestMapping("/api/orders")
@Component("orderController")
public class OrderController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public void createOrder(@RequestBody final OrderRequest orderRequest)
            throws CustomNotFoundException,
            CustomNotAuthorizedException,
            CustomBadRequestException {
        if (orderRequest.getBalance() <= 0) {
            log.error("Invalid balance order : {}", orderRequest.getBalance());
            throw new CustomBadRequestException("Invalid balance order : " + orderRequest.getBalance());
        }

        Account source = accountService.get(orderRequest.getSource());
        Account destination = accountService.get(orderRequest.getDestination());
        source.take(orderRequest.getBalance());
        destination.add(orderRequest.getBalance());

        accountService.update(source);
        accountService.update(destination);
    }

}
