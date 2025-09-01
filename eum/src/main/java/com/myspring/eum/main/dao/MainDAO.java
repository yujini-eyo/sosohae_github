package com.myspring.eum.main.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.eum.main.vo.MainVO;

public interface MainDAO {
    public List selectAllMainList() throws DataAccessException;
    public int insertMain(MainVO mainVO) throws DataAccessException;
    public int deleteMain(String id) throws DataAccessException;
    public MainVO selectMain(MainVO mainVO) throws DataAccessException;
}
