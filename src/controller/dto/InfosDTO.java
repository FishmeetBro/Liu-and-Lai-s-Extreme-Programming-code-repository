package com.farmland.intel.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author baixiaofa
 * @Date 2024/11/23 20:54
 * @description:
 */
@Data
public class InfosDTO {

    private Long id;

    List<String> phoneList;

    List<String> emailList;

    List<String> qqList;

    List<String> addressList;

}
