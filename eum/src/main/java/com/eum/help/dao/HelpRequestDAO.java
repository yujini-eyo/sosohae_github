package com.eum.help.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eum.help.vo.HelpRequestVO;
import com.eum.help.vo.RequestStatus;

@Mapper // 나중에 @MapperScan(또는 MapperScannerConfigurer)로 이 패키지를 스캔할 예정
public interface HelpRequestDAO {

    // 목록 조회 (페이징)
    List<HelpRequestVO> findAll(@Param("offset") int offset,
                                @Param("limit") int limit);

    // 단건 조회
    HelpRequestVO findById(@Param("id") Long id);

    // 등록 (Mapper XML에서 useGeneratedKeys로 PK 세팅)
    int insert(HelpRequestVO vo);

    // 상태 변경
    int updateStatus(@Param("id") Long id,
                     @Param("status") RequestStatus status);
}

