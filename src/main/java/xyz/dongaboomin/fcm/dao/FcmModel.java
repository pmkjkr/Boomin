package xyz.dongaboomin.fcm.dao;

import xyz.dongaboomin.fcm.dto.CircleFcmDTO;
import xyz.dongaboomin.fcm.dto.NormalFcmDTO;

import java.util.List;

public interface FcmModel {
    List<CircleFcmDTO> showCircleNotice(int limit, int offset, int admin_id);
    List<NormalFcmDTO> showNormalNotice(int limit, int offset, int admin_id);
    long countNNTable(int admin_id);
    long countCNTable(int admin_id);
}
