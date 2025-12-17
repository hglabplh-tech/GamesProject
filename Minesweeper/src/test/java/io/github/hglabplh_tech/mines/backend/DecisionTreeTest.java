package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DecisionTreeTest {

    private DecisionTree theTree = null;

    @BeforeEach
    public void before () {
        this.theTree = new DecisionTree();
        Point rootP = new Point(0,0);
        ButtonDescription buttDescription = new ButtonDescription(Boolean.FALSE, SweepPointType.STARTPOINT);
        this.theTree.initRoot(new ButtonPoint(rootP, buttDescription));
    }

    @Test
    public void initRoot() {
        DecisionTree tree = new DecisionTree();
        Point rootP = new Point(8,10);
        ButtonDescription buttDescription = new ButtonDescription(Boolean.FALSE, SweepPointType.STARTPOINT);
        Point leftP = new Point(17,13);
        ButtonDescription buttDescrLeft = new ButtonDescription(Boolean.FALSE, SweepPointType.NORMALPOINT);
        DecisionTree.TreeElement rootElement = tree.initRoot(new ButtonPoint(rootP, buttDescription));
        DecisionTree.TreeElement leftElement = tree.newTreeElement(rootElement, DecisionTree.TreeElementType.LEFT,
                new ButtonPoint(leftP, buttDescrLeft));
        tree.insertLeft(rootElement, leftElement);
        assertEquals(rootElement.getLeft().getThisPoint(), leftElement.getThisPoint());
    }

    @Test
    public void newTreeElement() {
        Point rootP = new Point(8,10);
        ButtonDescription buttDescription = new ButtonDescription(Boolean.FALSE,
                SweepPointType.FIRST_BASE);
        Point point = new Point(17,13);
        ButtonPoint buttonPoint = new ButtonPoint(point, buttDescription);
        DecisionTree.TreeElement newElement = this.theTree
                .newTreeElement(this.theTree.getRoot(), DecisionTree.TreeElementType.LEFT, buttonPoint);
        assertEquals(newElement.getThisPoint(), buttonPoint);
    }

    @Test
    public void insertLeft() {
        DecisionTree.TreeElement newElement = makeNewElement(true, DecisionTree.TreeElementType.LEFT,
                SweepPointType.MINEPOINT, 10,10);
        this.theTree.insertLeft(this.theTree.getRoot(), newElement);
        assertEquals(this.theTree.getRoot().getLeft().getThisPoint(), newElement.getThisPoint());
    }

    @Test
    public void insertRight() {
        DecisionTree.TreeElement newElement = makeNewElement(true, DecisionTree.TreeElementType.RIGHT,
                SweepPointType.MINEPOINT, 10,10);
        this.theTree.insertRight(this.theTree.getRoot(), newElement);
        assertEquals(this.theTree.getRoot().getRight().getThisPoint(), newElement.getThisPoint());
    }

    @Test
    public void insertSibling() {
        DecisionTree.TreeElement newElement = makeNewElement(true, DecisionTree.TreeElementType.SIBLING,
                SweepPointType.MINEPOINT, 10,10);
        this.theTree.insertSibling(this.theTree.getRoot(),
                this.theTree.getRoot(), newElement);
        assertEquals(this.theTree.getRoot().getSibling().getThisPoint(), newElement.getThisPoint());
    }

    @Test
    public void insertLeftRight() {
        DecisionTree.TreeElement leftElement = makeNewElement(true, DecisionTree.TreeElementType.LEFT,
                SweepPointType.MINEPOINT, 10,10);
        DecisionTree.TreeElement rightElement = makeNewElement(true, DecisionTree.TreeElementType.RIGHT,
                SweepPointType.NORMALPOINT, 15,30);
        this.theTree.insertLeftRight(this.theTree.getRoot(), leftElement, rightElement);
        assertEquals(this.theTree.getRoot().getLeft().getThisPoint(), leftElement.getThisPoint());
        assertEquals(this.theTree.getRoot().getRight().getThisPoint(), rightElement.getThisPoint());
    }

    @Test
    public void insertLeftRightSibling() {
        DecisionTree.TreeElement leftElement = makeNewElement(true, DecisionTree.TreeElementType.LEFT,
                SweepPointType.MINEPOINT, 10,10);
        DecisionTree.TreeElement rightElement = makeNewElement(true, DecisionTree.TreeElementType.RIGHT,
                SweepPointType.NORMALPOINT, 15,30);
        DecisionTree.TreeElement siblingElement = makeNewElement(false, DecisionTree.TreeElementType.SIBLING, SweepPointType.SECOND_BASE, 16,35);
        this.theTree.insertLeftRightSibling(this.theTree.getRoot(), leftElement, rightElement, siblingElement);
        assertEquals(this.theTree.getRoot().getLeft().getThisPoint(), leftElement.getThisPoint());
        assertEquals(this.theTree.getRoot().getRight().getThisPoint(), rightElement.getThisPoint());
        assertEquals(this.theTree.getRoot().getSibling().getThisPoint(), siblingElement.getThisPoint());
    }

    @Test
    public void addSuccessor () {
        DecisionTree.TreeElement element = makeNewElement(false, DecisionTree.TreeElementType.LEFT,
                SweepPointType.NORMALPOINT, 7, 7);
        Point p = new Point(7,7);
        element.addSuccessor(element, (e -> e.equalsInPoint(p)), element.getThisPoint());
        assertTrue(element.isSuccess());
        Point p2 = new Point(7,0);
        element.addSuccessor(element, (e -> e.equalsInPoint(p2)), element.getThisPoint());
        assertFalse(element.isSuccess());
    }

    @Test
    public void traverse() {

    }

    public DecisionTree.TreeElement makeNewElement(Boolean processed, DecisionTree.TreeElementType elementType,
                                                   SweepPointType pointType, int x, int y) {
        ButtonDescription buttDescription = new ButtonDescription(processed,
                pointType);
        Point point = new Point(x, y);
        ButtonPoint buttonPoint = new ButtonPoint(point, buttDescription);
        return this.theTree.newTreeElement(this.theTree.getRoot(), elementType, buttonPoint);
    }
}