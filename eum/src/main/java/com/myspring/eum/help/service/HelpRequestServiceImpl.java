package com.myspring.eum.help.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.eum.help.dao.HelpRequestDAO;
import com.myspring.eum.help.vo.HelpRequestVO;
import com.myspring.eum.help.vo.RequestStatus;


@Service("helpRequestService")
@Transactional(propagation = Propagation.REQUIRED)
public class HelpRequestServiceImpl implements HelpRequestService {
	
	@Autowired
	private HelpRequestDAO helpRequestDAO;

	@Override
	public long create(HelpRequestVO vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Optional<HelpRequestVO> getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HelpRequestVO> getPage(int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeStatus(long id, RequestStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scheduleAt(long id, LocalDateTime when) {
		// TODO Auto-generated method stub
		
	}
}
