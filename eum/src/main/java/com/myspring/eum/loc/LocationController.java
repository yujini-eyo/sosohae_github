package com.myspring.eum.loc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/loc")
public class LocationController {

  @RequestMapping(
      value = "/update",
      method = RequestMethod.POST,
      consumes = "application/json",
      produces = "application/json")
  @ResponseBody
  public Map<String, Object> update(@RequestBody LocationDTO dto){
    Map<String, Object> res = new HashMap<String, Object>();
    if (dto == null || dto.getRoomId() == null || dto.getUserId() == null) {
      res.put("ok", Boolean.FALSE);
      res.put("msg", "roomId/userId required");
      return res;
    }
    if (dto.getTs() == 0L) dto.setTs(System.currentTimeMillis());
    LocationRepo.upsert(dto);
    res.put("ok", Boolean.TRUE);
    return res;
  }

  @RequestMapping(
      value = "/list",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseBody
  public Collection<LocationDTO> list(
      @RequestParam("roomId") String roomId,
      @RequestParam(value="since", required=false) Long since){
    // 간단 청소
    LocationRepo.cleanup(5L * 60L * 1000L);
    if (since != null) {
      return LocationRepo.listSince(roomId, since.longValue());
    }
    return LocationRepo.list(roomId);
  }
}