package com.myspring.eum.help.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.eum.help.vo.HelpRequestVO;

@Repository("helpRequestDAO") // �굹以묒뿉 @MapperScan(�삉�뒗 MapperScannerConfigurer)濡� �씠 �뙣�궎吏�瑜� �뒪罹뷀븷 �삁�젙
public class HelpRequestDAOImpl implements HelpRequestDAO {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<HelpRequestVO> findAll() throws DataAccessException {
		
		return null;
	}

	@Override
	public HelpRequestVO findById(String id) throws DataAccessException{
		return null;
	}

	@Override
	public int insert(HelpRequestVO vo) throws DataAccessException{
		return 0;
	}

	@Override
	public int updateStatus(HelpRequestVO vo) throws DataAccessException{
		return 0;
	}

	@Override
	public int updateScheduledAt(HelpRequestVO vo) throws DataAccessException{
		return 0;
	}

}
