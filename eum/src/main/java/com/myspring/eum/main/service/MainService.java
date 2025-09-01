package com.myspring.eum.main.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.eum.main.vo.MainVO;

public interface MainService {
    public List listMains() throws DataAccessException;
    public int addMain(MainVO mainVO) throws DataAccessException;
    public int removeMain(String id) throws DataAccessException;
    public MainVO getMain(MainVO mainVO) throws Exception;
}
