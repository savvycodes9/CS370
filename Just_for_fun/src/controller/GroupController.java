package controller;

import dao.GroupDAO;
import dao.GroupDAOImpl;
import model.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupController {

    private GroupDAO groupDAO;

    public GroupController() {
        this.groupDAO = new GroupDAOImpl();
    }

    // ---------------------------------------------------------
    // CREATE GROUP
    // ---------------------------------------------------------
    public boolean createGroup(String name, int ownerUserId) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Group name cannot be empty.");
            return false;
        }

        // New group starts with empty member list
        Group group = new Group(
                0,                   // auto-generated ID
                name,
                ownerUserId,
                new ArrayList<>()    // empty member list
        );

        // Owner is always a member
        group.getMembers().add(ownerUserId);

        return groupDAO.saveGroup(group);
    }

    // ---------------------------------------------------------
    // ADD MEMBER
    // ---------------------------------------------------------
    public boolean addUserToGroup(int groupId, int userId) {
        return groupDAO.addMemberToGroup(groupId, userId);
    }

    // ---------------------------------------------------------
    // REMOVE MEMBER
    // ---------------------------------------------------------
    public boolean removeUserFromGroup(int groupId, int userId) {
        return groupDAO.removeMemberFromGroup(groupId, userId);
    }

    // ---------------------------------------------------------
    // GET GROUP INFO
    // ---------------------------------------------------------
    public Group getGroupById(int groupId) {
        return groupDAO.getGroupById(groupId);
    }

    public List<Group> getGroupsForUser(int userId) {
        return groupDAO.getGroupsByMember(userId);
    }

    public List<Integer> getUsersInGroup(int groupId) {
        Group group = groupDAO.getGroupById(groupId);
        return group != null ? group.getMembers() : new ArrayList<>();
    }
}
