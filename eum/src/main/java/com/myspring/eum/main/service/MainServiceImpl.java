package com.myspring.eum.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.eum.main.dao.MainDAO;
import com.myspring.eum.main.vo.MainVO;

@Service("mainService")
@Transactional(propagation = Propagation.REQUIRED)
public class MainServiceImpl implements MainService {

    @Autowired
    private MainDAO mainDAO;

    @Override
    public List listMains() throws DataAccessException {
        List mainsList = null;
        mainsList = mainDAO.selectAllMainList();
        return mainsList;
    }

    @Override
    public int addMain(MainVO mainVO) throws DataAccessException {
        return mainDAO.insertMain(mainVO);
    }

    @Override
    public int removeMain(String id) throws DataAccessException {
        return mainDAO.deleteMain(id);
    }

    @Override
    public MainVO getMain(MainVO mainVO) throws Exception {
        return mainDAO.selectMain(mainVO); // DAO 메서드명이 다르면 getMain(...)으로 맞춰주세요.
    }
}
