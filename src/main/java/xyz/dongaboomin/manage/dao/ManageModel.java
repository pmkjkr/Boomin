package xyz.dongaboomin.manage.dao;

import xyz.dongaboomin.manage.dto.ManageDTO;
import xyz.dongaboomin.manage.dto.PartManageDTO;

import java.util.List;

public interface ManageModel {
    List<PartManageDTO> showUserManageList(int limit, int offset, int circle_id);
    long countTabel();
}
