package com.qrcafe.service.impl;

import com.qrcafe.entity.Image;
import com.qrcafe.repository.ImageRepository;
import com.qrcafe.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }
}
