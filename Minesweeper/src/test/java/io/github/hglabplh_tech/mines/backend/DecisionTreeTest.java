package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.games.backend.util.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionTreeTest {

    private DecisionTree theTree = null;

    @BeforeEach
    public void before () {
        this.theTree = new DecisionTree();
        Point rootP = new Point(0,0);
        ButtonStatus buttDescription = new ButtonStatus(Boolean.FALSE, SweepPointType.STARTPOINT);
        this.theTree.initRoot(new ButtonPoint(rootP, buttDescription));
    }

    @Test
    public void initRoot() {
        DecisionTree tree = new DecisionTree();
        Point rootP = new Point(8,10);
        ButtonStatus buttDescription = new ButtonStatus(Boolean.FALSE, SweepPointType.STARTPOINT);
        Point leftP = new Point(17,13);
        ButtonStatus buttDescrLeft = new ButtonStatus(Boolean.FALSE, SweepPointType.NORMALPOINT);
        DecisionTree.TreeElement rootElement = tree.initRoot(new ButtonPoint(rootP, buttDescription));
        DecisionTree.TreeElement leftElement = tree.newTreeElement(rootElement, DecisionTree.TreeElementType.LEFT,
                new ButtonPoint(leftP, buttDescrLeft));
        tree.insertLeft(rootElement, leftElement);
        Assertions.assertEquals(rootElement.getLeft().getThisPoint(), leftElement.getThisPoint());
    }

    @Test
    public void newTreeElement() {
        ButtonStatus buttDescription = new ButtonStatus(Boolean.FALSE,
                SweepPointType.FIRST_BASE);
        Point point = new Point(17,13);
        ButtonPoint buttonPoint = new ButtonPoint(point, buttDescription);
        DecisionTree.TreeElement newElement = this.theTree
                .newTreeElement(this.theTree.getRoot(), DecisionTree.TreeElementType.LEFT, buttonPoint);
        Assertions.assertEquals(newElement.getThisPoint(), buttonPoint);
    }

    @Test
    public void insertLeft() {
        DecisionTree.TreeElement newElement = makeNewElement(true, DecisionTree.TreeElementType.LEFT,
                SweepPointType.MINEPOINT, 10,10);
        this.theTree.insertLeft(this.theTree.getRoot(), newElement);
        Assertions.assertEquals(this.theTree.getRoot().getLeft().getThisPoint(), newElement.getThisPoint());
    }

    @Test
    public void insertRight() {
        DecisionTree.TreeElement newElement = makeNewElement(true, DecisionTree.TreeElementType.RIGHT,
                SweepPointType.MINEPOINT, 10,10);
        this.theTree.insertRight(this.theTree.getRoot(), newElement);
        Assertions.assertEquals(this.theTree.getRoot().getRight().getThisPoint(), newElement.getThisPoint());
    }

    @Test
    public void insertSibling() {
        DecisionTree.TreeElement newElement = makeNewElement(true, DecisionTree.TreeElementType.SIBLING,
                SweepPointType.MINEPOINT, 10,10);
        this.theTree.insertSibling(this.theTree.getRoot(),
                newElement);
        Assertions.assertEquals(this.theTree.getRoot().getSibling().getThisPoint(), newElement.getThisPoint());
    }

    @Test
    public void insertLeftRight() {
        DecisionTree.TreeElement leftElement = makeNewElement(true, DecisionTree.TreeElementType.LEFT,
                SweepPointType.MINEPOINT, 10,10);
        DecisionTree.TreeElement rightElement = makeNewElement(true, DecisionTree.TreeElementType.RIGHT,
                SweepPointType.NORMALPOINT, 15,30);
        this.theTree.insertLeftRight(this.theTree.getRoot(), leftElement, rightElement);
        Assertions.assertEquals(this.theTree.getRoot().getLeft().getThisPoint(), leftElement.getThisPoint());
        Assertions.assertEquals(this.theTree.getRoot().getRight().getThisPoint(), rightElement.getThisPoint());
    }

    @Test
    public void insertLeftRightSibling() {
        DecisionTree.TreeElement leftElement = makeNewElement(true, DecisionTree.TreeElementType.LEFT,
                SweepPointType.MINEPOINT, 10,10);
        DecisionTree.TreeElement rightElement = makeNewElement(true, DecisionTree.TreeElementType.RIGHT,
                SweepPointType.NORMALPOINT, 15,30);
        DecisionTree.TreeElement siblingElement = makeNewElement(false, DecisionTree.TreeElementType.SIBLING, SweepPointType.SECOND_BASE, 16,35);
        this.theTree.insertLeftRightSibling(this.theTree.getRoot(), leftElement, rightElement, siblingElement);
        Assertions.assertEquals(this.theTree.getRoot().getLeft().getThisPoint(), leftElement.getThisPoint());
        Assertions.assertEquals(this.theTree.getRoot().getRight().getThisPoint(), rightElement.getThisPoint());
        Assertions.assertEquals(this.theTree.getRoot().getSibling().getThisPoint(), siblingElement.getThisPoint());
    }

    @Test
    public void addSuccessor () {
        DecisionTree.TreeElement elementNext = makeNewElement(false, DecisionTree.TreeElementType.LEFT,
                SweepPointType.NORMALPOINT, 7, 7);
        DecisionTree.TreeElement elementNoSuccMine = makeNewElement(false, DecisionTree.TreeElementType.RIGHT,
                SweepPointType.MINEPOINT, 45, 30);
        DecisionTree.TreeElement elementNoSuccPos = makeNewElement(false, DecisionTree.TreeElementType.LEFT,
                SweepPointType.NORMALPOINT, 47, 11);
        DecisionTree.TreeElement elementFinish = makeNewElement(false, DecisionTree.TreeElementType.PARENT,
                SweepPointType.ENDPOINT, 50, 50);
        Point p = new Point(7,7);
        Point p1 = new Point(45,30);
        Point p2 = new Point(50, 50);
        elementNext.addSuccessor((e -> (e.equalsInPoint(p) && !e.buttonStatus().isMine())) ,
                (e -> (e.equalsInPoint(p2) && !e.buttonStatus().isMine())),
                elementNext.getThisPoint());
        Assertions.assertTrue(elementNext.successIndicator().success());
        Assertions.assertFalse(elementNext.successIndicator().finished());
        Assertions.assertEquals(DecisionTree.SuccessIndicator.SUCCESSFUL, elementNext.successIndicator().indicator());

        elementNoSuccMine.addSuccessor(
                (e -> (e.equalsInPoint(p1)&& !e.buttonStatus().isMine())),
                (e -> (e.equalsInPoint(p2)&& !e.buttonStatus().isMine())),
                elementNoSuccMine.getThisPoint());
        Assertions.assertFalse(elementNoSuccMine.successIndicator().success());
        Assertions.assertFalse(elementNoSuccMine.successIndicator().finished());
        Assertions.assertEquals(DecisionTree.SuccessIndicator.NOK, elementNoSuccMine.successIndicator().indicator());


        elementNoSuccPos.addSuccessor(
                (e -> (e.equalsInPoint(p1)&& !e.buttonStatus().isMine())),
                (e -> (e.equalsInPoint(p2)&& !e.buttonStatus().isMine())),
                elementNoSuccPos.getThisPoint());
        Assertions.assertFalse(elementNoSuccPos.successIndicator().success());
        Assertions.assertFalse(elementNoSuccPos.successIndicator().finished());
        Assertions.assertEquals(DecisionTree.SuccessIndicator.NOK, elementNoSuccPos.successIndicator().indicator());

        elementFinish.addSuccessor(
                (e -> e.equalsInPoint(p1)),
                (e -> e.equalsInPoint(p2)),
                elementFinish.getThisPoint());
        Assertions.assertFalse(elementFinish.successIndicator().success());
        Assertions.assertTrue(elementFinish.successIndicator().finished());
        Assertions.assertFalse(elementFinish.successIndicator().isFinishItem());
        Assertions.assertEquals(DecisionTree.SuccessIndicator.FINISHED, elementFinish.successIndicator().indicator());

        elementFinish.addSuccessor(
                (e -> e.equalsInPoint(p2)),
                (e -> e.equalsInPoint(p2)),
                elementFinish.getThisPoint());
        Assertions.assertTrue(elementFinish.successIndicator().success());
        Assertions.assertTrue(elementFinish.successIndicator().finished());
        Assertions.assertTrue(elementFinish.successIndicator().isFinishItem());
        Assertions.assertEquals(DecisionTree.SuccessIndicator.FIN_SUCCESS, elementFinish.successIndicator().indicator());
    }

    @Test
    public void traverse() {

    }

    public DecisionTree.TreeElement makeNewElement(Boolean processed, DecisionTree.TreeElementType elementType,
                                                   SweepPointType pointType, int x, int y) {
        ButtonStatus buttDescription = new ButtonStatus(processed,
                pointType);
        Point point = new Point(x, y);
        ButtonPoint buttonPoint = new ButtonPoint(point, buttDescription);
        return this.theTree.newTreeElement(this.theTree.getRoot(), elementType, buttonPoint);
    }
}