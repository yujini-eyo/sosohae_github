package com.eum.help.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eum.help.dao.HelpRequestDAO;
import com.eum.help.vo.HelpRequestVO;
import com.eum.help.vo.RequestStatus;

@Service
@Transactional(readOnly = true)
public class HelpRequestServiceImpl implements HelpRequestService {
	private final HelpRequestDAO helpRequestDAO;

	public HelpRequestServiceImpl(HelpRequestDAO helpRequestDAO) {
		this.helpRequestDAO = helpRequestDAO;
	}
	// 쓰기 메서드(@Transactional)만 별도로 선언

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
