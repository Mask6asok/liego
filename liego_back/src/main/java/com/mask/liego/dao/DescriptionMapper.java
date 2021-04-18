package com.mask.liego.dao;

import com.mask.liego.entity.Description;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DescriptionMapper {
    public void insertDescription(Description description);

    public void updateDescripText(Description description);

    public void updateDescripImg1(Description description);

    public void updateDescripImg2(Description description);

    public void updateDescripImg3(Description description);

    public void deleteDescriptionByDid(int did);

    public Description getDescriptionByDid(int did);
}
