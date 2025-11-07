import java.util.ArrayList;
import java.util.List;

public class Group {
    private int groupId;
    private String groupName;
    private List<Integer> memberUserIds;

    public Group(int groupId, String groupName, List<Integer> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberUserIds = members != null ? members : new ArrayList<>();
    }

    public int getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public List<Integer> getMemberUserIds() { return memberUserIds; }

    public void addMember(int userId) {
        if (!memberUserIds.contains(userId)) {
            memberUserIds.add(userId);
        }
    }

    public void removeMember(int userId) {
        memberUserIds.remove(Integer.valueOf(userId));
    }
}
