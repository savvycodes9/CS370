package dao;

import model.Group;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAOImpl implements GroupDAO {

    private static final String GROUP_FILE = Paths.get("Just_for_fun", "Database", "groups.txt").toString();
    private static final String MEMBER_FILE = Paths.get("Just_for_fun", "Database", "group_members.txt").toString();

    public GroupDAOImpl() {
        ensureFilesExist();
    }

    private void ensureFilesExist() {
        try {
            Files.createDirectories(Paths.get("Just_for_fun", "Database"));
            if (!Files.exists(Paths.get(GROUP_FILE))) Files.createFile(Paths.get(GROUP_FILE));
            if (!Files.exists(Paths.get(MEMBER_FILE))) Files.createFile(Paths.get(MEMBER_FILE));
        } catch (IOException ignored) {}
    }

    // ========= Utility =========

    private int getNextGroupId(List<Group> groups) {
        int max = 0;
        for (Group g : groups) if (g.getGroupId() > max) max = g.getGroupId();
        return max + 1;
    }

    private List<Integer> loadMembers(int groupId) {
        List<Integer> members = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 2) {
                    int gid = Integer.parseInt(p[0]);
                    int uid = Integer.parseInt(p[1]);
                    if (gid == groupId) members.add(uid);
                }
            }
        } catch (IOException ignored) {}

        return members;
    }

    // ========= CRUD IMPLEMENTATION =========

    @Override
    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(GROUP_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 3) {
                    int id = Integer.parseInt(p[0]);
                    String name = p[1];
                    int owner = Integer.parseInt(p[2]);

                    Group g = new Group(id, name, owner, loadMembers(id));
                    groups.add(g);
                }
            }
        } catch (IOException ignored) {}

        return groups;
    }

    @Override
    public Group getGroupById(int groupId) {
        for (Group g : getAllGroups()) {
            if (g.getGroupId() == groupId)
                return g;
        }
        return null;
    }

    @Override
    public List<Group> getGroupsByMember(int userId) {
        List<Group> result = new ArrayList<>();

        for (Group g : getAllGroups()) {
            if (g.getMembers().contains(userId))
                result.add(g);
        }
        return result;
    }

    @Override
    public boolean saveGroup(Group group) {
        List<Group> groups = getAllGroups();
        int newId = getNextGroupId(groups);
        group.setGroupId(newId);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GROUP_FILE, true))) {
            bw.write(newId + "," + group.getName() + "," + group.getOwnerUserId());
            bw.newLine();
        } catch (IOException e) {
            return false;
        }

        // Save members
        for (int uid : group.getMembers()) {
            addMemberToGroup(newId, uid);
        }

        return true;
    }

    @Override
    public boolean updateGroup(Group group) {
        List<Group> groups = getAllGroups();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GROUP_FILE))) {
            for (Group g : groups) {
                if (g.getGroupId() == group.getGroupId()) {
                    bw.write(g.getGroupId() + "," + group.getName() + "," + group.getOwnerUserId());
                } else {
                    bw.write(g.getGroupId() + "," + g.getName() + "," + g.getOwnerUserId());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteGroup(int groupId) {
        List<Group> groups = getAllGroups();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GROUP_FILE))) {
            for (Group g : groups) {
                if (g.getGroupId() != groupId) {
                    bw.write(g.getGroupId() + "," + g.getName() + "," + g.getOwnerUserId());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            return false;
        }

        // Remove from group_members
        try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE));
             BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBER_FILE + ".tmp"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (Integer.parseInt(p[0]) != groupId) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException ignored) {}

        new File(MEMBER_FILE + ".tmp").renameTo(new File(MEMBER_FILE));

        return true;
    }

    @Override
    public boolean addMemberToGroup(int groupId, int userId) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBER_FILE, true))) {
            bw.write(groupId + "," + userId);
            bw.newLine();
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public boolean removeMemberFromGroup(int groupId, int userId) {
        try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE));
             BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBER_FILE + ".tmp"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                int gid = Integer.parseInt(p[0]);
                int uid = Integer.parseInt(p[1]);

                if (!(gid == groupId && uid == userId)) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) { return false; }

        new File(MEMBER_FILE + ".tmp").renameTo(new File(MEMBER_FILE));
        return true;
    }
}
