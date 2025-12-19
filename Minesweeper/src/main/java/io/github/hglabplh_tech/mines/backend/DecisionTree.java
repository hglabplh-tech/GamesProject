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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class DecisionTree {

    private TreeElement root = new TreeElement()
            .newBuilder()
            .build();


    public DecisionTree() {

    }

    public TreeElement initRoot(ButtonPoint rootPoint) {
        TreeElement newRoot = root.changeBuilder()
                .thisPointOpt(rootPoint)
                .build();
        this.root = newRoot;
        return this.root;
    }

    public TreeElement newTreeElement(TreeElement parent, TreeElementType type, ButtonPoint value) {
        return this.root.copyBuilder()
                .parentOpt(parent)
                .elementTypeOpt(type)
                .thisPointOpt(value).build();
    }


    public TreeElement insertLeft(TreeElement parent, TreeElement left) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .leftOpt(left)
                .build();
    }

    public TreeElement insertRight(TreeElement parent, TreeElement right) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .rightOpt(right)
                .build();

    }

    public TreeElement insertSibling(TreeElement parent, TreeElement element, TreeElement sibling) {
        return element.changeBuilder()
                .parentOpt(parent)
                .siblingOpt(sibling)
                .build();

    }

    public TreeElement insertLeftRight(TreeElement parent, TreeElement left,
                                       TreeElement right) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .leftOpt(left)
                .rightOpt(right)
                .build();

    }

    public TreeElement insertLeftRightSibling(TreeElement parent, TreeElement left,
                                       TreeElement right, TreeElement sibling) {
        return parent.changeBuilder()
                .parentOpt(parent)
                .leftOpt(left)
                .rightOpt(right)
                .siblingOpt(sibling)
                .build();

    }


    // have to think about that it is not complete
    public TreeElement traverse(TreeElement element) {
        TreeElement result = element;
        if (element.successIndicator().indicator().equals(SuccessIndicator.NOK)) {
            if (element.getSibling() != null) {
                result = traverse(element.getSibling());
            }

            if (element.getLeft() != null) {
                result = traverse(element.getLeft());
            }

            if (element.getRight() != null) {
                result = traverse(element.getRight());
            }

            if (element.getParent() != null) {
                result = traverse(element.getParent());
            }
            return result;
        } else {
            return element;
        }
    }

    public TreeElement getRoot() {
        return root;
    }

    public static class TreeElement {
        private ButtonPoint thisPoint;
        private TreeElementType elementType;
        private TreeElement parent;
        private TreeElement left;
        private TreeElement right;
        private TreeElement sibling;
        private SuccessIndicator successIndicator = new SuccessIndicator(false, false, SuccessIndicator.NOK);




        public TreeElement() {
        }

        public ButtonPoint getThisPoint() {
            return this.thisPoint;
        }

        public TreeElement getParent() {
            return this.parent;
        }

        public TreeElement getLeft() {
            return this.left;
        }

        public TreeElement getRight() {
            return this.right;
        }

        public TreeElement getSibling() {
            return this.sibling;
        }

        public SuccessIndicator successIndicator() {
            return this.successIndicator;
        }

        public TreeElementType getElementType () {
            return this.elementType;
        }

        public <T> TreeElement addSuccessor (TreeElement element,
                                         Predicate<? super ButtonPoint> predicateNext,
                                             Predicate<? super ButtonPoint> predicateEnd,
                                         ButtonPoint value) {
            return element
                    .changeBuilder()
                    .setSuccessor(predicateNext, predicateEnd, value)
                    .build();
        }

        public List<TreeElement> loopAndAddSuccessor (TreeElement element,
                                                          Predicate<? super ButtonPoint> predicateNext,
                                                          Predicate<? super ButtonPoint> predicateEnd,
                                                          ButtonPoint value) {
            List<TreeElement> result = new ArrayList<>();
            List<TreeElement> allElements = new ArrayList<>();
            if (element.getSibling() != null) {
                allElements.add(element.getSibling());
            }
            if (element.getLeft() != null) {
                allElements.add(element.getLeft());
            }
            if (element.getRight() != null) {
                allElements.add(element.getRight());
            }
            if (element.getParent() != null) {
                allElements.add(element.getParent());
            }

            for (TreeElement item: allElements) {
                TreeElement withSuccessor = item.addSuccessor(item, predicateNext, predicateEnd, value);
                if (withSuccessor.successIndicator().indicator().equals(SuccessIndicator.SUCCESSFUL) ||
                        withSuccessor.successIndicator().indicator().equals(SuccessIndicator.FIN_SUCCESS)) {
                    result.add(withSuccessor);
                } else {
                    result = loopAndAddSuccessor(item, predicateNext, predicateEnd, value);
                }
            }
            return result;
        }

        public Builder newBuilder() {
            return new Builder();
        }

        public Builder copyBuilder() {
            return new Builder(this, true);
        }

        public Builder changeBuilder() {
            return new Builder(this, false);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            TreeElement that = (TreeElement) o;
            return Objects.equals(getThisPoint(), that.getThisPoint())
                    && getElementType() == that.getElementType()
                    && Objects.equals(getParent(), that.getParent())
                    && Objects.equals(getLeft(), that.getLeft())
                    && Objects.equals(getRight(), that.getRight())
                    && Objects.equals(getSibling(), that.getSibling())
                    && Objects.equals(successIndicator(), that.successIndicator());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getThisPoint(),
                    getElementType(), getParent(),
                    getLeft(), getRight(),
                    getSibling(), successIndicator());
        }

        @Override
        public String toString() {
            return "TreeElement{" +
                    "thisPoint=" + getThisPoint() +
                    ", elementType=" + getElementType() +
                    ", parent=" + getParent() +
                    ", left=" + getLeft() +
                    ", right=" + getRight() +
                    ", sibling=" + getSibling() +
                    ", successIndicator=" + successIndicator() +
                    '}';
        }

        public static class Builder {
            private Optional<ButtonPoint> thisPointOpt = Optional.empty();
            private Optional<TreeElementType> elementTypeOpt = Optional.empty();
            private Optional<SuccessIndicator> successOpt = Optional.empty();
            private Optional<TreeElement> parentOpt = Optional.empty();
            private Optional<TreeElement> leftOpt = Optional.empty();
            private Optional<TreeElement> rightOpt = Optional.empty();
            private Optional<TreeElement> siblingOpt = Optional.empty();

            private TreeElement newElement = null;

            public Builder() {
                this.newElement = new TreeElement();
            }

            public Builder(TreeElement toCopy, boolean copy) {
                this.thisPointOpt = Optional.ofNullable(toCopy.getThisPoint());
                this.elementTypeOpt = Optional.ofNullable(toCopy.getElementType());
                this.parentOpt = Optional.ofNullable(toCopy.getParent());
                this.leftOpt = Optional.ofNullable(toCopy.getLeft());
                this.rightOpt = Optional.ofNullable(toCopy.getRight());
                this.siblingOpt = Optional.ofNullable(toCopy.getSibling());
                this.successOpt = Optional.ofNullable(toCopy.successIndicator());
                if (copy) {
                    this.newElement = new TreeElement();
                } else {
                    this.newElement = toCopy;
                }

            }

            public Builder thisPointOpt(ButtonPoint point) {
                this.thisPointOpt = Optional.of(point);
                return this;
            }

            public Builder elementTypeOpt(TreeElementType type) {
                this.elementTypeOpt = Optional.of(type);
                return this;
            }

            public Builder parentOpt(TreeElement parent) {
                this.parentOpt = Optional.of(parent);
                return this;
            }


            public Builder leftOpt(TreeElement left) {
                this.leftOpt = Optional.of(left);
                return this;
            }

            public Builder rightOpt(TreeElement right) {
                this.rightOpt = Optional.of(right);
                return this;
            }

            public Builder siblingOpt(TreeElement right) {
                this.siblingOpt = Optional.of(right);
                return this;
            }

            public  Builder setSuccessor(Predicate<? super ButtonPoint> predicateNext,
                                            Predicate<? super ButtonPoint> predicateEnd,
                                            ButtonPoint value) {
               if (predicateNext != null && predicateEnd != null && value != null) {
                   Boolean success = predicateNext.test(value);
                   Boolean finished = predicateEnd.test(value);
                   Integer indicator = findIndicator(success, finished);
                   this.successOpt = Optional.of(
                           new SuccessIndicator(success,
                                   finished, indicator));
               } else {
                   this.successOpt = Optional.of(new SuccessIndicator(Boolean.FALSE,
                           Boolean.FALSE, SuccessIndicator.NOK));
               }
               return this;
            }

            private Integer findIndicator(Boolean success, Boolean finished) {
                Integer indicator = SuccessIndicator.NOK;
                if (!success && finished) {
                    indicator = SuccessIndicator.FINISHED;
                } else {
                    if (success) {
                        if (finished) {
                            indicator = SuccessIndicator.FIN_SUCCESS;
                        } else {
                            indicator = SuccessIndicator.SUCCESSFUL;
                        }
                    }
                }
                return indicator;
            }

            public TreeElement build() {
                TreeElement element = this.newElement;
                element.thisPoint = this.thisPointOpt.orElse(null);
                element.elementType = this.elementTypeOpt.orElse(null);
                element.parent = this.parentOpt.orElse(null);
                element.left = this.leftOpt.orElse(null);
                element.right = this.rightOpt.orElse(null);
                element.sibling = this.siblingOpt.orElse(null);
                element.successIndicator = this.successOpt.orElse(null);
                return element;
            }

        }
    }

    public enum TreeElementType {
        LEFT("left"),
        RIGHT("right"),
        PARENT("parent"),
        SIBLING("sibling");

        private final String typeName;

        TreeElementType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }

        @Override
        public String toString() {
            return "TreeElementType{" +
                    "typeName='" + typeName + '\'' +
                    '}';
        }
    }

    public static class SuccessIndicator {
        private final Boolean success;

        private final Boolean finished;

        private final Integer indicator;

        public static final Integer SUCCESSFUL = 0x01;
        public static final Integer FINISHED = 0x02;
        public static final Integer FIN_SUCCESS = SUCCESSFUL + FINISHED;
        public static final Integer NOK = 0x10;


        public SuccessIndicator(Boolean success, Boolean finished, Integer indicator) {
            this.success = success;
            this.finished = finished;
            this.indicator = indicator;
        }

        public Boolean success() {
            return this.success;
        }

        public Integer indicator() {
            return this.indicator;
        }

        public Boolean finished() {
            return this.finished;
        }

        public Boolean isFinishItem() {
            return success() && finished();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            SuccessIndicator that = (SuccessIndicator) o;
            return Objects.equals(success, that.success)
                    && Objects.equals(finished, that.finished)
                    && Objects.equals(indicator, that.indicator);
        }

        @Override
        public int hashCode() {
            return Objects.hash(success, finished, indicator);
        }

        @Override
        public String toString() {
            return "SuccessIndicator{" +
                    "success=" + success +
                    ", finished=" + finished +
                    ", indicator=" + indicator +
                    '}';
        }
    }
}
