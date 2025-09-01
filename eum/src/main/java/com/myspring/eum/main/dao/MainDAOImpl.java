package com.myspring.eum.main.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.eum.main.vo.MainVO;

@Repository("mainDAO")
public class MainDAOImpl implements MainDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List selectAllMainList() throws DataAccessException {
        List<MainVO> mainsList = null;
        mainsList = sqlSession.selectList("mapper.main.selectAllMainList");
        return mainsList;
    }

    @Override
    public int insertMain(MainVO mainVO) throws DataAccessException {
        int result = sqlSession.insert("mapper.main.insertMain", mainVO);
        return result;
    }

    @Override
    public int deleteMain(String id) throws DataAccessException {
        int result = sqlSession.delete("mapper.main.deleteMain", id);
        return result;
    }

    @Override
    public MainVO selectMain(MainVO mainVO) throws DataAccessException {
        MainVO vo = sqlSession.selectOne("mapper.main.selectMain", mainVO);
        return vo;
    }
}
