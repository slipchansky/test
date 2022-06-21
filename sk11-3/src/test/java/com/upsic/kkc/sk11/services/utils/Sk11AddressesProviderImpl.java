package com.upsic.kkc.sk11.services.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.upsic.kkc.sk11.dto.Sk11AddressDto;


@Service
public class Sk11AddressesProviderImpl implements Sk11AddressesProvider {
    
    @Value("${sk11.base-path}")
    private String address; 
    
    public List<Sk11AddressDto> getSk11AddressList() {
      return Arrays.asList(Sk11AddressDto.builder().address(address).build());  
    }

}
