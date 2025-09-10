package com.spring.E_Learning.Config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dipvbaqbv",
                "api_key", "473117489337384",
                "api_secret", "nHgZrzJRkan0gLbPVCrYYVUWDVs"));
    }
    }

