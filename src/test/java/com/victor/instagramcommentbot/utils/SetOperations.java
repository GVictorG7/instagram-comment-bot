package com.victor.instagramcommentbot.utils;

import java.util.HashSet;
import java.util.Set;

public class SetOperations {
  public static <E> Set<E> difference(Set<E> set1, Set<E> set2) {
    Set<E> difference = new HashSet<>(set1);
    difference.removeAll(set2);
    return difference;
  }

  public static <E> Set<E> intersection(Set<E> set1, Set<E> set2) {
    Set<E> intersection = new HashSet<>(set1);
    intersection.retainAll(set2);
    return intersection;
  }

  public static <E> Set<E> union(Set<E> set1, Set<E> set2) {
    Set<E> union = new HashSet<>(set1);
    union.addAll(set2);
    return union;
  }
}
