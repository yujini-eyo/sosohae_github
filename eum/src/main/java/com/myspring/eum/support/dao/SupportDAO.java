// SupportDAO.java
// 역할: 매퍼 호출 메서드 인터페이스 정의 (서비스에서 호출)
package com.myspring.eum.support.dao;

import java.util.List;
import com.myspring.eum.support.vo.SupportApplicationVO;

public interface SupportDAO {
	long insert(SupportApplicationVO vo); // 지원 등록

	int exists(int articleNO, String volunteerId); // 중복 체크

	List<SupportApplicationVO> listByArticle(int articleNO); // 글별 신청자 목록

	List<SupportApplicationVO> findByVolunteerId(String volunteerId); // 내 신청 목록

	int updateStatus(long applicationId, String status); // 상태 변경

	int deleteById(long applicationId); // 삭제
}
