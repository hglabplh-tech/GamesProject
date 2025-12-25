package io.github.hglabplh_tech.mines.backend.util;

import io.github.hglabplh_tech.mines.backend.ButtonPoint;
import io.github.hglabplh_tech.mines.backend.DecisionTree;
import io.github.hglabplh_tech.mines.backend.DecisionTree.TreeElement;
import io.github.hglabplh_tech.mines.backend.DecisionTree.TreeElementType;

public class DecisionTreeUtils {

    private DecisionTreeUtils() {

    }

    public static TreeElement insertElementLeftRight(DecisionTree tree,
                                                     TreeElement parent,
                                                     TreeElementType type,
                                                     ButtonPoint newPoint) {
        TreeElement newElement = tree.newTreeElement(parent, type, newPoint);
        return switch (type) {
            case LEFT -> tree.insertLeft(parent, newElement);
            case RIGHT -> tree.insertRight(parent, newElement);
            default -> null;
        };
    }


    public static TreeElement insertSibling( DecisionTree tree,
                                            TreeElement parent,
                                            TreeElement sibling,
                                            ButtonPoint newPoint) {
        TreeElement newElement = tree.newTreeElement(parent, TreeElementType.SIBLING, newPoint);
        return tree.insertSibling(parent, sibling, newElement);
    }
}
