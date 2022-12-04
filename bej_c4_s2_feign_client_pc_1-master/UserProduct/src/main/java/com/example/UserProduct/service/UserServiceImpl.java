package com.example.UserProduct.service;

import com.example.UserProduct.domain.Product;
import com.example.UserProduct.domain.User;
import com.example.UserProduct.exception.ProductNotFoundException;
import com.example.UserProduct.exception.UserAlreadyFoundException;
import com.example.UserProduct.exception.UserNotFoundException;
import com.example.UserProduct.proxy.UserProxy;
import com.example.UserProduct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProxy userProxy;

    public UserServiceImpl(UserRepository userRepository,UserProxy userProxy){
        this.userRepository=userRepository;
        this.userProxy=userProxy;
    }
    @Override
    public User addUser(User user) throws UserAlreadyFoundException {
        if(userRepository.findById(user.getUserId()).isPresent()) {
            throw new UserAlreadyFoundException();
        }
        User user1 = userRepository.save(user);
        if(!(user1.getUserId().isEmpty())){
            ResponseEntity responseEntity = userProxy.saveUser(user);
            System.out.println(responseEntity.getBody());
        }
        return user1;
    }


    @Override
    public User addProductForUser(String userId, Product product) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userRepository.findByUserId(userId);
        if(user.getProductList()==null){
            user.setProductList(Arrays.asList(product));
        }else {
            List<Product> products=user.getProductList();
            products.add(product);
            user.setProductList(products);
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteProductFromUser(String userId, int productId) throws UserNotFoundException ,ProductNotFoundException{
        boolean result=false;
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(userId).get();
        List<Product> products=user.getProductList();
        result=products.removeIf(x->x.getProductId()==productId);
        if(!result){
            throw new ProductNotFoundException();
        }
        user.setProductList(products);
        return userRepository.save(user);
    }

    @Override
    public List<Product> getProductForUser(String userId) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        return userRepository.findById(userId).get().getProductList();
    }


}
