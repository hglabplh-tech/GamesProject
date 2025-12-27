package io.github.hglabplh_tech.mines.backend.util;
/*
Copyright (c) 2025 Harald Glab-Plhak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
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
        return tree.insertSibling(sibling, newElement);
    }
}
