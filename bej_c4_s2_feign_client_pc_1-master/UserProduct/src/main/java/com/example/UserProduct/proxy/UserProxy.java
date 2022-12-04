package com.example.UserProduct.proxy;

import com.example.UserProduct.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name = "authenticationService",url = "http://localhost:8099")
public interface UserProxy {

        @PostMapping("/user/v1/register")
        ResponseEntity<?> saveUser(@RequestBody User user);

}
