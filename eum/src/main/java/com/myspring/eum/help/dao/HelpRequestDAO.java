package com.myspring.eum.help.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.myspring.eum.help.vo.HelpRequestVO;
import com.myspring.eum.help.vo.RequestStatus;

public interface HelpRequestDAO {

	// 紐⑸줉 議고쉶 (�럹�씠吏�)
	public List<HelpRequestVO> findAll() throws DataAccessException;

	// �떒嫄� 議고쉶
	public HelpRequestVO findById(String id) throws DataAccessException;

	// �벑濡� (Mapper XML�뿉�꽌 useGeneratedKeys濡� PK �꽭�똿)
	public int insert(HelpRequestVO vo) throws DataAccessException ;

	// �긽�깭 蹂�寃�
	public int updateStatus(HelpRequestVO vo) throws DataAccessException ;

	
	public int updateScheduledAt(HelpRequestVO vo) throws DataAccessException ;
}
