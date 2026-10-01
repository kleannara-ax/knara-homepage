package com.kleannara.service;

import com.kleannara.mapper.CustomerMapper;
import com.kleannara.model.CustomerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    @Autowired
    public CustomerMapper customerMapper;

    public List<CustomerModel> getContents(String category, String version){

        CustomerModel customerModel = new CustomerModel();

        customerModel.setCategory(category);
//        if(!version.equals("")){
//            customerModel.setVersion(version);
//        }

        List<CustomerModel> customerModelList = customerMapper.getCustomerText(customerModel);

        return customerModelList;

    };
}
