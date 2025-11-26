package model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private int groupId;
    private String name;
    private int ownerUserId;
    private List<Integer> members;

    public Group(int groupId, String name, int ownerUserId, List<Integer> members) {
        this.groupId = groupId;
        this.name = name;
        this.ownerUserId = ownerUserId;
        this.members = (members != null) ? members : new ArrayList<>();

        // Ensure owner is always included as a member
        if (!this.members.contains(ownerUserId)) {
            this.members.add(ownerUserId);
        }
    }


    // Convenience constructor for creating new groups
    public Group(int groupId, String name, int ownerUserId) {
        this.groupId = groupId;
        this.name = name;
        this.ownerUserId = ownerUserId;

        this.members = new ArrayList<>();
        this.members.add(ownerUserId);
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    // Getters
    public int getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public List<Integer> getMembers() {
        return members;
    }

    // Add/remove members
    public void addMember(int userId) {
        if (!members.contains(userId)) {
            members.add(userId);
        }
    }

    public void removeMember(int userId) {
        members.remove(Integer.valueOf(userId));
    }

    public void setName(String name) {
        this.name = name;
    }

}
