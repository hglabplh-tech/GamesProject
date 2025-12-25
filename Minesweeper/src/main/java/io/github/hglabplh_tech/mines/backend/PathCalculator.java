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
package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.util.DecisionTreeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PathCalculator {

    private final SweeperLogic util;
    private final Labyrinth labyrinth;

    private final DecisionTree theTree;

    public PathCalculator(SweeperLogic util, Labyrinth labyrinth) {
        this.theTree = new DecisionTree();
        this.util = util;
        this.labyrinth = labyrinth;
    }

    public List<ButtonPoint> calculatePath(ButtonPoint start, ButtonPoint end) {
        List<ButtonPoint> path = new ArrayList<>();
        List<List<ButtonDescription>> field = this.getUtil().getFieldsList();
        ButtonDescription descriptionStart = field.get(start.myPoint().y()).get(start.myPoint().x());
        ButtonDescription descriptionEnd = field.get(end.myPoint().y()).get(end.myPoint().x());
        ButtonPoint startPoint = new ButtonPoint(start.myPoint(), descriptionStart);
        ButtonPoint endPoint = new ButtonPoint(end.myPoint(), descriptionEnd);
        if (startPoint.equals(start) && endPoint.equals(end)) {
            this.getTheTree().initRoot(startPoint);
            DecisionTree.TreeElement leftElement =
                    DecisionTreeUtils.insertElementLeftRight(this.getTheTree(),
                            this.theTree.getRoot(),
                            DecisionTree.TreeElementType.LEFT,
                            endPoint);
            Conditions conds = registerConditions(startPoint, endPoint, startPoint);
            leftElement = leftElement.addSuccessor(leftElement, conds.nextCond(), conds.endCond(), leftElement.getThisPoint());

        }
        return path;
    }


    public DecisionTree getTheTree() {
        return this.theTree;
    }

    public SweeperLogic getUtil() {
        return this.util;
    }

    public Labyrinth getLabyrinth() {
        return this.labyrinth;
    }

    public Conditions registerConditions(ButtonPoint startPoint, ButtonPoint endPoint,
                                         ButtonPoint actualPoint) {
        return null;
    }

    private ButtonPoint calculateNextPoint(DecisionTree.TreeElement node) {
        return null;
    }

    public static  class Conditions{
        private final Predicate<ButtonPoint> nextCond;

        private final Predicate<ButtonPoint> endCond;

        public Conditions(Predicate<ButtonPoint> nextCond, Predicate<ButtonPoint> endCond) {
            this.nextCond = nextCond;
            this.endCond = endCond;
        }

        public Predicate<ButtonPoint> nextCond() {
            return nextCond;
        }

        public Predicate<ButtonPoint> endCond() {
            return endCond;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Conditions that = (Conditions) o;
            return Objects.equals(nextCond, that.nextCond) && Objects.equals(endCond, that.endCond);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nextCond, endCond);
        }
    }
}
