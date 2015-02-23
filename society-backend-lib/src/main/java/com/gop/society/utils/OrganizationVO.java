package com.gop.society.utils;

import lombok.Data;

import java.util.Set;

/**
 * @author GhostOfPQ
 */
@Data
public class OrganizationVO {
    private String id;
    private String name;
    private String description;

    private Set<UserOrgaVO> admins;
    private Set<UserOrgaVO> members;

    private Set<AccountVOUserView> accounts;
    private Set<CurrencyVOUserView> managedCurrencies;
}
