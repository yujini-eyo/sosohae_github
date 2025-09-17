package com.myspring.eum.support.service;

import java.util.List;
import com.myspring.eum.support.vo.SupportApplicationVO;

public interface SupportService {
	void apply(int articleNo, String volunteerId, String message);

	void cancel(long applicationId, String operatorId);

	List<SupportApplicationVO> listApplicants(int articleNo, String requesterId);

	void select(long applicationId, String requesterId);

	void reopen(int articleNo, String requesterId); // optional

	List<SupportApplicationVO> listMyApplications(String volunteerId);
}
