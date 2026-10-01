package com.kleannara.mapper;

import com.kleannara.model.CustomerModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomerMapper {

    public List<CustomerModel> getCustomerText(CustomerModel customerModel);
}
