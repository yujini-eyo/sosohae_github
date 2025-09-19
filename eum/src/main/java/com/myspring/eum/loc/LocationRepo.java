package com.myspring.eum.loc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocationRepo {
  // roomId -> (userId -> dto)
  private static final ConcurrentMap<String, ConcurrentMap<String, LocationDTO>> store =
      new ConcurrentHashMap<String, ConcurrentMap<String, LocationDTO>>();

  public static void upsert(LocationDTO dto){
    ConcurrentMap<String, LocationDTO> room = store.get(dto.getRoomId());
    if (room == null) {
      ConcurrentMap<String, LocationDTO> newMap = new ConcurrentHashMap<String, LocationDTO>();
      ConcurrentMap<String, LocationDTO> prev = store.putIfAbsent(dto.getRoomId(), newMap);
      room = (prev != null) ? prev : newMap;
    }
    room.put(dto.getUserId(), dto);
  }

  public static Collection<LocationDTO> list(String roomId){
    ConcurrentMap<String, LocationDTO> m = store.get(roomId);
    if (m == null) return Collections.<LocationDTO>emptyList();
    return new ArrayList<LocationDTO>(m.values()); // 복사본 반환
  }

  public static Collection<LocationDTO> listSince(String roomId, long since){
    ConcurrentMap<String, LocationDTO> m = store.get(roomId);
    if (m == null) return Collections.<LocationDTO>emptyList();
    ArrayList<LocationDTO> out = new ArrayList<LocationDTO>();
    for (LocationDTO d : m.values()) {
      if (d.getTs() >= since) out.add(d);
    }
    return out;
  }

  public static void cleanup(long maxAgeMs){
    long now = System.currentTimeMillis();
    for (Map.Entry<String, ConcurrentMap<String, LocationDTO>> e : store.entrySet()) {
      ConcurrentMap<String, LocationDTO> room = e.getValue();
      Iterator<Map.Entry<String, LocationDTO>> it = room.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<String, LocationDTO> en = it.next();
        LocationDTO d = en.getValue();
        if (now - d.getTs() > maxAgeMs) {
          it.remove();
        }
      }
    }
  }
}