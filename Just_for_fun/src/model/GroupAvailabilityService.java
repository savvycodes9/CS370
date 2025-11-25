package model;

import model.Availability;
import model.Group;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class GroupAvailabilityService {

    /**
     * Returns a list of intervals (start, end) when ALL members are available.
     */
    public List<Interval> computeGroupAvailability(
            Group group, List<Availability> allAvailabilities, String date) {

        // 1. Filter only availability for members + matching date + available=true
        Map<Integer, List<Availability>> perUser = allAvailabilities.stream()
                .filter(a -> group.getMembers().contains(a.getUserId()))
                .filter(a -> a.getDate().equals(date))
                .filter(Availability::isAvailable)
                .collect(Collectors.groupingBy(Availability::getUserId));

        // Edge case: user has no availability → group unavailable
        if (perUser.values().stream().anyMatch(List::isEmpty)) {
            return List.of();
        }

        // Convert each user's availability into merged intervals
        List<List<Interval>> usersIntervals = new ArrayList<>();

        for (int userId : group.getMembers()) {
            List<Availability> userAv = perUser.getOrDefault(userId, List.of());
            usersIntervals.add(mergeIntervals(userAv));
        }

        // Intersect across all members
        return intersectAll(usersIntervals);
    }


    // -------------------- Interval Class -------------------------
    public static class Interval {
        public LocalTime start;
        public LocalTime end;

        public Interval(LocalTime s, LocalTime e) {
            this.start = s;
            this.end = e;
        }

        @Override
        public String toString() {
            return start + " - " + end;
        }
    }

    // -------------------- Merge intervals for one user ------------------------
    private List<Interval> mergeIntervals(List<Availability> list) {
        List<Interval> intervals = list.stream()
                .map(a -> new Interval(
                        LocalTime.parse(a.getStartTime()),
                        LocalTime.parse(a.getEndTime())))
                .sorted(Comparator.comparing(i -> i.start))
                .collect(Collectors.toList());

        if (intervals.isEmpty()) return intervals;

        List<Interval> merged = new ArrayList<>();
        Interval current = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            Interval next = intervals.get(i);

            if (!next.start.isAfter(current.end)) {
                current.end = next.end.isAfter(current.end) ? next.end : current.end;
            } else {
                merged.add(current);
                current = next;
            }
        }

        merged.add(current);
        return merged;
    }

    // -------------------- Intersection across all users ------------------------
    private List<Interval> intersectAll(List<List<Interval>> users) {
        // Start with the first user's intervals
        List<Interval> result = new ArrayList<>(users.get(0));

        for (int i = 1; i < users.size(); i++) {
            result = intersectTwo(result, users.get(i));
            if (result.isEmpty()) break;
        }

        return result;
    }

    private List<Interval> intersectTwo(List<Interval> A, List<Interval> B) {
        List<Interval> output = new ArrayList<>();

        int i = 0, j = 0;
        while (i < A.size() && j < B.size()) {
            Interval a = A.get(i);
            Interval b = B.get(j);

            LocalTime latestStart = a.start.isAfter(b.start) ? a.start : b.start;
            LocalTime earliestEnd = a.end.isBefore(b.end) ? a.end : b.end;

            if (!latestStart.isAfter(earliestEnd)) {
                output.add(new Interval(latestStart, earliestEnd));
            }

            if (a.end.isBefore(b.end)) i++;
            else j++;
        }

        return output;
    }
}
