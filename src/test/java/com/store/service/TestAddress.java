package com.store.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.dto.AddressDTO;

@SpringBootTest
public class TestAddress {

    @Autowired
    private UserService service;

    @Test
    public void saveAddress() {
        float latitude = (float) -82.3582992553711;
        float longitude = (float) 23.1359004974365;
        AddressDTO addressDTO = new AddressDTO("98 entre 53 y 55", "5318A",
                latitude, longitude, "Mi casa");
        AddressDTO newAddress = service.setAddress(addressDTO, Long.valueOf(1));
        assertNotNull(newAddress);
    }

    @Test
    public void updateAddress() {
        AddressDTO updateAddress = new AddressDTO();
        updateAddress.setId(Long.valueOf(1));
        updateAddress.setAlias("Casa de mi madre");
        updateAddress.setFormatted("98 entre 53 y 55");
        updateAddress.setApto("6789B");
        AddressDTO updatedAddress = service.setAddress(updateAddress, Long.valueOf(1));
        assertNotNull(updatedAddress);

    }

    @Test
    public void deleteAddress() {
        service.deleteAddress(Long.valueOf(2));
    }

}
