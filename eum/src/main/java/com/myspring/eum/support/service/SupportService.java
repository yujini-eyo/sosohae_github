// SupportService.java
// 역할: 컨트롤러가 의존하는 비즈니스 규칙 정의
package com.myspring.eum.support.service;

import java.util.List;
import com.myspring.eum.support.vo.SupportApplicationVO;

public interface SupportService {
	long apply(int articleNO, String volunteerId, String message); // 지원하기

	boolean exists(int articleNO, String volunteerId); // 중복 여부

	List<SupportApplicationVO> listByArticle(int articleNO); // 글별 신청자 목록

	List<SupportApplicationVO> listMyApplications(String volunteerId); // 내 신청 목록

	void select(long applicationId); // 선정(SELECTED)

	void reject(long applicationId); // 거절(REJECTED)

	void withdraw(long applicationId); // 철회(WITHDRAWN)
}
