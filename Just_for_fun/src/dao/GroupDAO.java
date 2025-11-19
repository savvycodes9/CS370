package dao;

import model.Group;
import java.util.List;

public interface GroupDAO {

    List<Group> getAllGroups();
    Group getGroupById(int groupId);
    List<Group> getGroupsByMember(int userId);

    boolean saveGroup(Group group);
    boolean updateGroup(Group group);
    boolean deleteGroup(int groupId);

    boolean addMemberToGroup(int groupId, int userId);
    boolean removeMemberFromGroup(int groupId, int userId);
}
