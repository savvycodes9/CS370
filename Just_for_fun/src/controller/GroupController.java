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

    // Create group
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

    // Add member
    public boolean addUserToGroup(int groupId, int userId) {
        return groupDAO.addMemberToGroup(groupId, userId);
    }

    // Remove user
    public boolean removeUserFromGroup(int groupId, int userId) {
        return groupDAO.removeMemberFromGroup(groupId, userId);
    }

    // Get group info
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

    public Group getGroupByName(String name) {
        return groupDAO.getGroupByName(name);
    }

    public boolean updateGroup(int groupId, String newName) {
        Group group = groupDAO.getGroupById(groupId);

        if (group == null) {
            return false;
        }

        group.setName(newName);
        return groupDAO.updateGroup(group);
    }


}
