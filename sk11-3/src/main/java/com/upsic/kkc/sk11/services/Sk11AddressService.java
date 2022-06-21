package com.upsic.kkc.sk11.services;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.upsic.kkc.sk11.dto.Sk11AddressDto;
import com.upsic.kkc.sk11.services.utils.Sk11AddressesProvider;
import com.upsic.kkc.sk11.utils.Sk11Constants;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Sk11AddressService {

    private final Sk11AddressesProvider sk11AddressRepository;
    private final static Comparator<Sk11AddressDto> SK_11_ADDRESS_COMPARATOR = Comparator.comparing(Sk11AddressDto::getPriority);

    @Cacheable(Sk11Constants.Cache.SK11_ADDRESS_ENTITIES_CACHE)
    public List<String> getSk11AddressListInternal() {
        return sk11AddressRepository.getSk11AddressList()
                .stream()
                .sorted(SK_11_ADDRESS_COMPARATOR)
                .map(Sk11AddressDto::getAddress)
                .collect(Collectors.toList());
    }

}
